package ru.samtakoy.domain.theme.di

import dagger.Component
import ru.samtakoy.data.di.DataModuleApiComponent
import ru.samtakoy.domain.qpack.di.QPackDomainApiComponent

@ThemeDomainScope
@Component(
    modules = [ThemeDomainModule::class],
    dependencies = [
        DataModuleApiComponent::class,
        QPackDomainApiComponent::class
    ]
)
interface ThemeDomainImplComponent : ThemeDomainApiComponent