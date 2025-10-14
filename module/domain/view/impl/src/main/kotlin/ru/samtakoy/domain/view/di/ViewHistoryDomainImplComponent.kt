package ru.samtakoy.domain.view.di

import dagger.Component
import ru.samtakoy.data.di.DataModuleApiComponent
import ru.samtakoy.domain.di.CardDomainApiComponent

@ViewHistoryDomainScope
@Component(
    modules = [ViewHistoryDomainModule::class],
    dependencies = [
        DataModuleApiComponent::class,
        CardDomainApiComponent::class
    ]
)
interface ViewHistoryDomainImplComponent : ViewHistoryDomainApiComponent