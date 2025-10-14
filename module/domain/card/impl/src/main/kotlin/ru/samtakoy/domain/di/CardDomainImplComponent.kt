package ru.samtakoy.domain.di

import dagger.Component
import ru.samtakoy.data.di.DataModuleApiComponent

@CardDomainScope
@Component(
    modules = [CardDomainModule::class],
    dependencies = [DataModuleApiComponent::class]
)
interface CardDomainImplComponent : CardDomainApiComponent