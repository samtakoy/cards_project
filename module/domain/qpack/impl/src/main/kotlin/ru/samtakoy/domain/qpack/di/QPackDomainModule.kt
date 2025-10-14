package ru.samtakoy.domain.qpack.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.qpack.QPackInteractorImpl

@Module
internal interface QPackDomainModule {
    @Binds
    fun bindsQPackInteractor(impl: QPackInteractorImpl): QPackInteractor
}