package ru.samtakoy.domain.theme.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.domain.theme.ThemeInteractor
import ru.samtakoy.domain.theme.ThemeInteractorImpl

fun themeFeatureModule() = module {
    factoryOf(::ThemeInteractorImpl) bind ThemeInteractor::class
}