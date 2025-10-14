package ru.samtakoy.domain.qpack.di

import dagger.Component
import ru.samtakoy.data.di.DataModuleApiComponent
// import ru.samtakoy.domain.view.di.ViewHistoryDomainApiComponent
// import ru.samtakoy.platform.di.PlatformApiComponent

@QPackDomainScope
@Component(
    modules = [
        QPackDomainModule::class
    ],
    dependencies = [
        DataModuleApiComponent::class,
        // ViewHistoryDomainApiComponent::class,
        // PlatformApiComponent::class
    ]
)
interface QPackDomainImplComponent : QPackDomainApiComponent