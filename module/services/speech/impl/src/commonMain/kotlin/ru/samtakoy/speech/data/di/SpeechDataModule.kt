package ru.samtakoy.speech.data.di

import nl.marc_apps.tts.experimental.ExperimentalDesktopTarget
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose
import ru.samtakoy.speech.data.repo.PlayerStateRepositoryImpl
import ru.samtakoy.speech.domain.repo.PlayerRepository
import ru.samtakoy.speech.data.repo.PlayerRepositoryImpl
import ru.samtakoy.speech.domain.repo.PlayerStateRepository
import ru.samtakoy.speech.domain.scope.PlayerScopeQualifier
import ru.samtakoy.speech.domain.scope.PlayerStateScopeQualifier

fun speechDataModule() = module {
    scope(qualifier = PlayerScopeQualifier) {
        @OptIn(ExperimentalDesktopTarget::class)
        scoped<PlayerRepository> {
            PlayerRepositoryImpl(
                factory = get(),
                scopeProvider = get()
            )
        }
        .onClose { repository ->
            if (repository != null) {
                (repository as PlayerRepositoryImpl).close()
            }
        }
    }
    scope(qualifier = PlayerStateScopeQualifier) {
        scopedOf(::PlayerStateRepositoryImpl) bind PlayerStateRepository::class
    }
}