package ru.samtakoy.platform.speech

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.getString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.domain.speech.PlayCardsAudioUseCase
import ru.samtakoy.domain.speech.model.SpeechPlaybackState
import ru.samtakoy.platform.notification.AndroidNotificationRepositoryImpl
import ru.samtakoy.platform.speech.PlayCardsAudioBroadcastReceiver.Companion.ACTION_NEXT
import ru.samtakoy.platform.speech.PlayCardsAudioBroadcastReceiver.Companion.ACTION_PAUSE
import ru.samtakoy.platform.speech.PlayCardsAudioBroadcastReceiver.Companion.ACTION_PREVIOUS
import ru.samtakoy.platform.speech.PlayCardsAudioBroadcastReceiver.Companion.ACTION_RESUME
import ru.samtakoy.platform.speech.PlayCardsAudioBroadcastReceiver.Companion.ACTION_STOP
import ru.samtakoy.platform.speech.impl.R
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.player_init_desc
import ru.samtakoy.resources.player_init_title

internal class PlayCardsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(
    appContext,
    params
), KoinComponent {

    private val playCardsAudioUseCase: PlayCardsAudioUseCase by inject()
    private val notificationRepository: AndroidNotificationRepositoryImpl by inject()
    private val scopeProvider: ScopeProvider by inject()
    private var currentState: SpeechPlaybackState? = null
    private val remoteViews: RemoteViews by lazy {
        createRemoteViews()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notification = buildNotificationFromState(currentState)
        return buildForegroundInfo(notification)
    }

    override suspend fun doWork(): Result {
        setForeground(getForegroundInfo())
        return try {
            playCardsAudioUseCase.observePlaybackState()
                .onEach {
                    currentState = it
                    setForeground(getForegroundInfo())
                }
                .launchIn(
                    scopeProvider.mainScope
                )
            playCardsAudioUseCase.playCards(
                cardIds = inputData.getLongArray(PARAM_CARD_IDS)!!.toList(),
                onlyQuestions = inputData.getBoolean(PARAM_ONLY_QUESTIONS, true)
            )
            Result.success()
        } catch (e: Exception) {
            Napier.e(e) { "PlayCardsWorker error" }
            Result.failure()
        }
    }

    private suspend fun buildNotificationFromState(state: SpeechPlaybackState?): Notification {
        return if (state == null) {
            notificationRepository.buildSpeechNotification(
                remoteViews = getInitRemoteViews()
                    .applySettings(
                        title = getString(Res.string.player_init_title),
                        text = getString(Res.string.player_init_desc),
                        curProgress = 0,
                        maxProgress = 1
                    ),
                clickIntent = null
            )
        } else {
            notificationRepository.buildSpeechNotification(
                remoteViews = getRemoteViewsWhen(isPlaying = state.isPaused.not())
                    .applySettings(
                        title = state.title,
                        text = state.description,
                        curProgress = state.currentCardNum,
                        maxProgress = state.totalCards
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
    
    private fun createRemoteViews(): RemoteViews {
        val remoteViews = RemoteViews(
            applicationContext.packageName,
            R.layout.player_notification
        )
        remoteViews.setOnClickPendingIntent(R.id.prevBtn, createActionIntent(ACTION_PREVIOUS))
        remoteViews.setOnClickPendingIntent(R.id.resumeBtn, createActionIntent(ACTION_RESUME))
        remoteViews.setOnClickPendingIntent(R.id.pauseBtn, createActionIntent(ACTION_PAUSE))
        remoteViews.setOnClickPendingIntent(R.id.nextBtn, createActionIntent(ACTION_NEXT))
        remoteViews.setOnClickPendingIntent(R.id.stopBtn, createActionIntent(ACTION_STOP))
        return remoteViews
    }

    private fun getRemoteViewsWhen(isPlaying: Boolean) : RemoteViews {
        return remoteViews.also { resultViews ->
            resultViews.setViewVisibility(R.id.buttons, View.VISIBLE)
            if (isPlaying) {
                resultViews.setViewVisibility(R.id.resumeBtn, View.GONE)
                resultViews.setViewVisibility(R.id.pauseBtn, View.VISIBLE)
            } else {
                resultViews.setViewVisibility(R.id.resumeBtn, View.VISIBLE)
                resultViews.setViewVisibility(R.id.pauseBtn, View.GONE)
            }
        }
    }

    private fun getInitRemoteViews() : RemoteViews {
        return remoteViews.also { resultViews ->
            resultViews.setViewVisibility(R.id.buttons, View.GONE)
        }
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