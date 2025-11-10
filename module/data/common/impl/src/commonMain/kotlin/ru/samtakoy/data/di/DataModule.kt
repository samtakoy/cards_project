package ru.samtakoy.data.di

import org.koin.dsl.module
import ru.samtakoy.data.card.di.cardDataModule
import ru.samtakoy.data.cardtag.di.cardTagDataModule
import ru.samtakoy.data.common.di.commonDataModule
import ru.samtakoy.data.common.di.commonPlatformDataModule
import ru.samtakoy.data.learncourse.di.learnCourseDataModule
import ru.samtakoy.data.learncourseview.di.learnCourseViewDataModule
import ru.samtakoy.data.qpack.di.qPackDataModule
import ru.samtakoy.data.theme.di.themeDataModule
import ru.samtakoy.data.view.di.viewDataModule

fun dataModule() = module {
    includes(
        commonDataModule(),
        commonPlatformDataModule(),
        cardDataModule(),
        cardTagDataModule(),
        learnCourseDataModule(),
        learnCourseViewDataModule(),
        qPackDataModule(),
        themeDataModule(),
        viewDataModule()
    )
}