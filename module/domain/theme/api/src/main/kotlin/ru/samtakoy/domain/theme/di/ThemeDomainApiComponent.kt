package ru.samtakoy.domain.theme.di

import ru.samtakoy.domain.theme.ThemeInteractor

interface ThemeDomainApiComponent {
    fun themeInteractor(): ThemeInteractor
}