package ru.samtakoy.data.di

import dagger.Component
import ru.samtakoy.common.di.CommonUtilsComponent
import ru.samtakoy.data.card.di.CardDataModule
import ru.samtakoy.data.cardtag.di.CardTagDataModule
import ru.samtakoy.data.common.di.CommonDataModule
import ru.samtakoy.data.learncourse.di.LearnCourseDataModule
import ru.samtakoy.data.learncourseview.di.LearnCourseViewDataModule
import ru.samtakoy.data.qpack.di.QPackDataModule
import ru.samtakoy.data.theme.di.ThemeDataModule
import ru.samtakoy.data.view.di.ViewDataModule
import ru.samtakoy.platform.di.PlatformApiComponent

@DataScope
@Component(
    modules = [
        CommonDataModule::class,
        CardDataModule::class,
        CardTagDataModule::class,
        QPackDataModule::class,
        ThemeDataModule::class,
        LearnCourseDataModule::class,
        LearnCourseViewDataModule::class,
        ViewDataModule::class
    ],
    dependencies = [
        CommonUtilsComponent::class,
        PlatformApiComponent::class,
    ]
)
interface DataModuleImplComponent : DataModuleApiComponent