package ru.samtakoy.domain.qpack.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.qpack.QPackInteractorImpl

fun qPackFeatureModule() = module {
    factoryOf(::QPackInteractorImpl) bind QPackInteractor::class
}