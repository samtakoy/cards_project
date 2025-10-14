package ru.samtakoy.domain.learncourse.di

import dagger.Component
import ru.samtakoy.data.di.DataModuleApiComponent
import ru.samtakoy.domain.qpack.di.QPackDomainApiComponent
import ru.samtakoy.domain.view.di.ViewHistoryDomainApiComponent
import ru.samtakoy.platform.di.PlatformApiComponent

@LearnCourseDomainScope
@Component(
    modules = [LearnCourseDomainModule::class],
    dependencies = [
        DataModuleApiComponent::class,
        ViewHistoryDomainApiComponent::class,
        QPackDomainApiComponent::class,
        PlatformApiComponent::class
    ]
)
interface LearnCourseDomainImplComponent : LearnCourseDomainApiComponent