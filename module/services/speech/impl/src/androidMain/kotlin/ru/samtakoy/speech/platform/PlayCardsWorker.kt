package ru.samtakoy.speech.platform

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.getString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.scope.Scope
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.common.utils.scope.releaseScope
import ru.samtakoy.common.utils.scope.useScope
import ru.samtakoy.services.speech.impl.R
import ru.samtakoy.platform.notification.AndroidNotificationRepository
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.player_init_desc
import ru.samtakoy.resources.player_init_title
import ru.samtakoy.speech.domain.PlayCardsAudioUseCase
import ru.samtakoy.speech.domain.ReadSpeechPlayerStateUseCase
import ru.samtakoy.speech.domain.model.SpeechPlayerState
import ru.samtakoy.speech.domain.scope.PlayerScopeQualifier
import ru.samtakoy.speech.platform.mapper.PlayerNotificationTextMapper

/**
 * TODO DataStore для текущего стейта плейера
 *  + восстановление состояния (номер карточки и статус проигрывателя)
 * */
internal class PlayCardsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(
    appContext,
    params
), KoinComponent {

    private val notificationRepository: AndroidNotificationRepository by inject()
    private val scopeProvider: ScopeProvider by inject()
    private val textMapper: PlayerNotificationTextMapper by inject()
    private var currentState: SpeechPlayerState? = null
    private val remoteViewsBig: RemoteViews by lazy {
        createRemoteViews(shortView = false)
    }
    private val remoteViewsShort: RemoteViews by lazy {
        createRemoteViews(shortView = true)
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notification = buildNotificationFromState(currentState)
        return buildForegroundInfo(notification)
    }

    override suspend fun doWork(): Result {
        setForeground(getForegroundInfo())
        val playerScope: Scope = getKoin().useScope(PlayerScopeQualifier)
        var subscriberJob: Job? = null
        return try {
            val playCardsAudioUseCase: PlayCardsAudioUseCase by playerScope.inject()
            val readPlayerStateUseCase: ReadSpeechPlayerStateUseCase by playerScope.inject()

            subscriberJob = readPlayerStateUseCase.observePlaybackState()
                .onEach { playerState ->
                    if (playerState is SpeechPlayerState.Active) {
                        currentState = playerState
                        setForeground(getForegroundInfo())
                    } else {
                        currentState = null
                    }
                }
                .launchIn(
                    scopeProvider.mainScope
                )
            playCardsAudioUseCase.playCards(
                cardIds = inputData.getLongArray(PARAM_CARD_IDS)!!.toList(),
                onlyQuestions = inputData.getBoolean(PARAM_ONLY_QUESTIONS, true)
            )
            Result.success()
        } catch (_: CancellationException) {
            // проигрывание отменено
            Result.success()
        } catch (e: Exception) {
            Napier.e(e) { "PlayCardsWorker error" }
            Result.failure()
        } finally {
            playerScope.releaseScope()
            subscriberJob?.cancel()
        }
    }

    private suspend fun buildNotificationFromState(state: SpeechPlayerState?): Notification {
        return if (state == null || state !is SpeechPlayerState.Active) {
            notificationRepository.buildSpeechNotification(
                remoteViewsBig = remoteViewsBig.initStart(),
                remoteViewsShort = remoteViewsShort.initStart(),
                clickIntent = null
            )
        } else {
            val record = state.curRecord
            notificationRepository.buildSpeechNotification(
                remoteViewsBig = remoteViewsBig.initWhen(isPlaying = state.isPaused.not())
                    .applySettings(
                        title = textMapper.mapBigNotificationTitle(state),
                        text = textMapper.mapBigNotificationDescription(state),
                        curProgress = record.cardNum,
                        maxProgress = state.cardIds.size
                    ),
                remoteViewsShort = remoteViewsShort.initWhen(isPlaying = state.isPaused.not())
                    .applySettings(
                        title = textMapper.mapSmallNotificationTitle(state),
                        text = textMapper.mapSmallNotificationDescription(state),
                        curProgress = record.cardNum,
                        maxProgress = state.cardIds.size
                    ),
                clickIntent = null
            )
        }
    }

    private fun buildForegroundInfo(notification: Notification): ForegroundInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                notificationRepository.getSpeechNotificationId(),
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            ForegroundInfo(
                notificationRepository.getSpeechNotificationId(),
                notification
            )
        }
    }

    private fun createActionIntent(action: String): PendingIntent {
        val intent = Intent(applicationContext, PlayCardsAudioBroadcastReceiver::class.java).apply {
            this.action = action
        }
        return PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    private fun createRemoteViews(shortView: Boolean): RemoteViews {
        val remoteViews = RemoteViews(
            applicationContext.packageName,
            if (shortView) R.layout.player_notification_short else R.layout.player_notification
        )
        remoteViews.setOnClickPendingIntent(R.id.prevBtn, createActionIntent(PlayCardsAudioBroadcastReceiver.ACTION_PREVIOUS))
        remoteViews.setOnClickPendingIntent(R.id.resumeBtn, createActionIntent(PlayCardsAudioBroadcastReceiver.ACTION_RESUME))
        remoteViews.setOnClickPendingIntent(R.id.pauseBtn, createActionIntent(PlayCardsAudioBroadcastReceiver.ACTION_PAUSE))
        remoteViews.setOnClickPendingIntent(R.id.nextBtn, createActionIntent(PlayCardsAudioBroadcastReceiver.ACTION_NEXT))
        remoteViews.setOnClickPendingIntent(R.id.stopBtn, createActionIntent(PlayCardsAudioBroadcastReceiver.ACTION_STOP))
        return remoteViews
    }

    private fun RemoteViews.initWhen(isPlaying: Boolean) : RemoteViews {
        setViewVisibility(R.id.buttons, View.VISIBLE)
        setViewVisibility(R.id.progress, View.VISIBLE)
        if (isPlaying) {
            setViewVisibility(R.id.resumeBtn, View.GONE)
            setViewVisibility(R.id.pauseBtn, View.VISIBLE)
        } else {
            setViewVisibility(R.id.resumeBtn, View.VISIBLE)
            setViewVisibility(R.id.pauseBtn, View.GONE)
        }
        return this
    }

    private suspend fun RemoteViews.initStart() : RemoteViews {
        setViewVisibility(R.id.buttons, View.GONE)
        setViewVisibility(R.id.progress, View.GONE)
        applySettings(
            title = getString(Res.string.player_init_title),
            text = getString(Res.string.player_init_desc),
            curProgress = 0,
            maxProgress = 1
        )
        return this
    }

    private fun RemoteViews.applySettings(
        title: String,
        text: String,
        curProgress: Int,
        maxProgress: Int
    ): RemoteViews {
        setTextViewText(R.id.title, title)
        setTextViewText(R.id.text, text)
        setProgressBar(R.id.progress, maxProgress, curProgress, false)
        return this
    }

    companion object {
        const val PARAM_CARD_IDS = "CARD_IDS"
        const val PARAM_ONLY_QUESTIONS = "ONLY_QUESTIONS"
    }
}