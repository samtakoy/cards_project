package ru.samtakoy.core.presentation.themes.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.presentation.themes.mapper.ThemeUiItemMapper
import ru.samtakoy.core.presentation.themes.mapper.ThemeUiItemMapperImpl

@Module
internal interface ThemesPresentationModule {
    @Binds
    fun bindsThemeUiItemMapper(impl: ThemeUiItemMapperImpl): ThemeUiItemMapper
}