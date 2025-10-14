package ru.samtakoy.domain.theme.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.domain.theme.ThemeInteractor
import ru.samtakoy.domain.theme.ThemeInteractorImpl

@Module
internal interface ThemeDomainModule {
    @Binds
    fun bindsThemeInteractor(impl: ThemeInteractorImpl): ThemeInteractor
}