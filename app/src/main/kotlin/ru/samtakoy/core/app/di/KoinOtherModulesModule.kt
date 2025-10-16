package ru.samtakoy.core.app.di

import org.koin.dsl.module
import ru.samtakoy.common.di.commonUtilsModule
import ru.samtakoy.core.presentation.courses.di.coursesPresentationModule
import ru.samtakoy.core.presentation.favorites.di.favoritesPresentationModule
import ru.samtakoy.core.presentation.schedule.di.schedulePresentationModule
import ru.samtakoy.data.di.dataModule
import ru.samtakoy.domain.di.cardDomainModule
import ru.samtakoy.domain.favorites.di.favoritesDomainModule
import ru.samtakoy.domain.learncourse.di.learnCourseDomainModule
import ru.samtakoy.domain.qpack.di.qPackDomainModule
import ru.samtakoy.domain.theme.di.themeDomainModule
import ru.samtakoy.domain.view.di.viewHistoryDomainModule
import ru.samtakoy.platform.di.platformModule

fun koinOtherModulesModule() = module {
    includes(
        commonUtilsModule(),

        dataModule(),

        cardDomainModule(),
        favoritesDomainModule(),
        learnCourseDomainModule(),
        qPackDomainModule(),
        themeDomainModule(),
        viewHistoryDomainModule(),
        platformModule(),

        coursesPresentationModule(),
        favoritesPresentationModule(),
        schedulePresentationModule()
    )
}