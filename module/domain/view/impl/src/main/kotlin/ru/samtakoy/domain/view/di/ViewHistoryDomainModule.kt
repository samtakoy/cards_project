package ru.samtakoy.domain.view.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.domain.view.ViewHistoryInteractorImpl

@Module
internal interface ViewHistoryDomainModule {
    @Binds
    fun bindsViewHistoryInteractor(impl: ViewHistoryInteractorImpl): ViewHistoryInteractor
}