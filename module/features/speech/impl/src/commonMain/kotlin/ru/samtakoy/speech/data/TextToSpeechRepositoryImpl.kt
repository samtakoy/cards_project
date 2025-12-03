package ru.samtakoy.speech.data

import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.marc_apps.tts.TextToSpeechFactory
import nl.marc_apps.tts.TextToSpeechInstance
import nl.marc_apps.tts.experimental.ExperimentalDesktopTarget
import nl.marc_apps.tts.experimental.ExperimentalVoiceApi
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.speech.domain.TextToSpeechRepository
import ru.samtakoy.speech.domain.model.TextToSpeechPlayer
import ru.samtakoy.speech.data.TextToSpeechPlayerImpl

internal class TextToSpeechRepositoryImpl(
    @OptIn(ExperimentalDesktopTarget::class)
    private val factory: TextToSpeechFactory,
    private val scopeProvider: ScopeProvider
) : TextToSpeechRepository, AutoCloseable {

    private var currentPlayer: TextToSpeechPlayerImpl? = null
    private val mutex = Mutex()

    override fun close() {
        scopeProvider.ioScope.launch {
            closeCurrentPlayer()
            scopeProvider.cancel()
        }
    }

    @OptIn(ExperimentalVoiceApi::class)
    override suspend fun getPlayer(): TextToSpeechPlayer? {
        return getOrCreatePlayer()
    }

    private suspend fun getOrCreatePlayer(): TextToSpeechPlayer? = mutex.withLock {
        if (currentPlayer == null || currentPlayer?.isClosed() == true) {
            currentPlayer = createPlayer()
        }
        return currentPlayer
    }

    private suspend fun closeCurrentPlayer() = mutex.withLock {
        try {
            currentPlayer?.let {
                if (!it.isClosed()) {
                    it.close()
                }
            }
        } catch (t: Throwable) {
            Napier.e("TextToSpeechRepositoryImpl::close error", t)
        }
    }

    private suspend fun createPlayer(): TextToSpeechPlayerImpl? {
        return try {
            @OptIn(ExperimentalDesktopTarget::class)
            factory.createOrNull()?.let {
                applyVoice(it)
                it.pitch = 1.0f  // Нормальный тон голоса
                it.rate = 1.0f   // Нормальная скорость
                it.volume = 100 // Полная громкость
                TextToSpeechPlayerImpl(it)
            }
        } catch (t: Throwable) {
            Napier.e("createPlayer() error", t)
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