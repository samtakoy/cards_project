package ru.samtakoy.speech.data

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import nl.marc_apps.tts.TextToSpeechInstance
import ru.samtakoy.speech.domain.model.TextToSpeechPlayer
import ru.samtakoy.speech.domain.model.TextToSpeechPlayerResult

internal class TextToSpeechPlayerImpl(
    private var ttsEngine: TextToSpeechInstance?
) : TextToSpeechPlayer {

    override suspend fun say(vararg text: String, pauseBetweenMs: Int): TextToSpeechPlayerResult {
        ttsEngine ?: return TextToSpeechPlayerResult.NotInited
        return try {
            text.forEachIndexed { idx, oneText ->
                ttsEngine?.say(
                    text = oneText,
                    clearQueue = true,
                    clearQueueOnCancellation = true
                )
                if (text.lastIndex != idx) {
                    delay(pauseBetweenMs.toLong())
                }
            }
            TextToSpeechPlayerResult.Succeed
        } catch (e: CancellationException) {
            TextToSpeechPlayerResult.Canceled
        } catch (t: Throwable) {
            Napier.e("say error", t)
            TextToSpeechPlayerResult.Failed(t)
        }
    }

    override fun stop() {
        ttsEngine?.stop()
    }

    override fun close() {
        ttsEngine?.let {
            ttsEngine = null
            it.stop()
            it.close()
        }
    }

    fun isClosed(): Boolean {
        return ttsEngine == null
    }
}