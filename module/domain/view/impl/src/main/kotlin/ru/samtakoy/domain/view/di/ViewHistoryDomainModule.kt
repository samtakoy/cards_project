package ru.samtakoy.domain.view.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.domain.view.ViewHistoryInteractorImpl

fun viewHistoryDomainModule() = module {
    factoryOf(::ViewHistoryInteractorImpl) bind ViewHistoryInteractor::class
}