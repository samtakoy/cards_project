package ru.samtakoy.data.speech

import io.github.aakira.napier.Napier
import nl.marc_apps.tts.TextToSpeechFactory
import nl.marc_apps.tts.TextToSpeechInstance
import nl.marc_apps.tts.experimental.ExperimentalDesktopTarget
import nl.marc_apps.tts.experimental.ExperimentalVoiceApi
import ru.samtakoy.domain.speech.model.TextToSpeechPlayer

@OptIn(ExperimentalDesktopTarget::class)
internal class TextToSpeechRepositoryImpl(
    private val factory: TextToSpeechFactory
) : TextToSpeechRepository {

    @OptIn(ExperimentalVoiceApi::class)
    override suspend fun createPlayer(): TextToSpeechPlayer? {
        return try {
            factory.createOrNull()?.let {
                applyVoice(it)
                // Опционально: настройте параметры
                it.pitch = 1.0f  // Нормальный тон голоса
                it.rate = 1.0f   // Нормальная скорость
                it.volume = 100 // Полная громкость
                TextToSpeechPlayerImpl(it)
            }
        } catch (e: Exception) {
            Napier.e("createPlayer() error", e)
            null
        }
    }

    @OptIn(ExperimentalVoiceApi::class)
    private fun applyVoice(instance: TextToSpeechInstance) {
        instance.voices.find {
            it.language.startsWith(RU_LANGUAGE)
        }?.let { voice ->
            instance.currentVoice = voice
        }
    }

    companion object {
        private const val RU_LANGUAGE = "ru"
    }
}