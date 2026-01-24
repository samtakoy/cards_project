package ru.samtakoy.speech.presentation.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.common.utils.scope.releaseScope
import ru.samtakoy.common.utils.scope.useScope
import ru.samtakoy.speech.domain.scope.PlayerStateScopeQualifier
import ru.samtakoy.speech.presentation.mapper.PlayerUiStateMapper
import ru.samtakoy.speech.presentation.mapper.PlayerUiStateMapperImpl
import ru.samtakoy.speech.presentation.vm.PlayerViewModelImpl

fun speechPresentationModule() = module {
    factoryOf(::PlayerUiStateMapperImpl) bind PlayerUiStateMapper::class
    viewModel {
        val playerStateScope = getKoin().useScope(PlayerStateScopeQualifier)
        PlayerViewModelImpl(
            playerStateReadUseCase = playerStateScope.get(),
            playerDispatcher = playerStateScope.get(),
            playerStateMapper = get(),
            scopeProvider = get()
        ) {
            playerStateScope.releaseScope()
        }
    }
}