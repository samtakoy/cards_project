package ru.samtakoy.presentation.main.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapperImpl
import ru.samtakoy.presentation.main.vm.MainScreenViewModelImpl

fun mainScreenPresentationModule() = module {
    factoryOf(::MainScreenContentMapperImpl) bind MainScreenContentMapper::class
    viewModelOf(::MainScreenViewModelImpl)
}