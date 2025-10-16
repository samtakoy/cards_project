package ru.samtakoy.core.presentation.themes.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.core.presentation.themes.mapper.ThemeUiItemMapper
import ru.samtakoy.core.presentation.themes.mapper.ThemeUiItemMapperImpl
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModelImpl

internal fun themesPresentationModule() = module {
    factoryOf(::ThemeUiItemMapperImpl) bind ThemeUiItemMapper::class
    viewModelOf(::ThemeListViewModelImpl)
}