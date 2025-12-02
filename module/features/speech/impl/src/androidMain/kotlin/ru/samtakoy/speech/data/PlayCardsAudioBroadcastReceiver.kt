package ru.samtakoy.speech.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.aakira.napier.Napier
import org.koin.java.KoinJavaComponent.getKoin
import ru.samtakoy.common.utils.Dispatcher
import ru.samtakoy.common.utils.scope.releaseScope
import ru.samtakoy.common.utils.scope.useScope
import ru.samtakoy.speech.domain.model.SpeechControlsListener
import ru.samtakoy.speech.domain.scope.PlayerStateScopeQualifier

class PlayCardsAudioBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        val action = intent.action ?: return
        Napier.e(tag = "mytest") { action }

        val playerStateScope = getKoin().useScope(PlayerStateScopeQualifier)
        try {
            // TODO пока через диспатчер, подумать/переделать на репозиторий с состоянием и событиями плейера
            val dispatcher: Dispatcher<SpeechControlsListener> by playerStateScope.inject()
            dispatchControlAction(dispatcher, action)
        } finally {
            playerStateScope.releaseScope()
        }

    }

    private fun dispatchControlAction(
        dispatcher: Dispatcher<SpeechControlsListener>,
        action: String
    ) {
        when (action) {
            ACTION_PAUSE -> dispatcher.dispatch { pausePlayback() }
            ACTION_RESUME -> dispatcher.dispatch { resumePlayback() }
            ACTION_NEXT -> dispatcher.dispatch { nextCard() }
            ACTION_PREVIOUS -> dispatcher.dispatch { previousCard() }
            ACTION_STOP -> dispatcher.dispatch { stopPlayback() }
        }
    }

    companion object {
        const val ACTION_PAUSE = "ru.samtakoy.action.PAUSE"
        const val ACTION_RESUME = "ru.samtakoy.action.RESUME"
        const val ACTION_NEXT = "ru.samtakoy.action.NEXT"
        const val ACTION_PREVIOUS = "ru.samtakoy.action.PREVIOUS"
        const val ACTION_STOP = "ru.samtakoy.action.STOP"
    }
}