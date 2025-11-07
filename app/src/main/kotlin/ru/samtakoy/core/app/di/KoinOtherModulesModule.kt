package ru.samtakoy.core.app.di

import org.koin.dsl.module
import ru.samtakoy.common.di.commonUtilsModule
import ru.samtakoy.core.presentation.courses.di.coursesPresentationModule
import ru.samtakoy.core.presentation.favorites.di.favoritesPresentationModule
import ru.samtakoy.core.presentation.schedule.di.schedulePresentationModule
import ru.samtakoy.data.di.dataModule
import ru.samtakoy.data.importcards.di.importDataModule
import ru.samtakoy.data.task.di.taskStateDataModule
import ru.samtakoy.domain.di.cardDomainModule
import ru.samtakoy.domain.favorites.di.favoritesDomainModule
import ru.samtakoy.domain.importcards.di.importCardsDomainModule
import ru.samtakoy.domain.learncourse.di.learnCourseDomainModule
import ru.samtakoy.domain.qpack.di.qPackDomainModule
import ru.samtakoy.domain.theme.di.themeDomainModule
import ru.samtakoy.domain.view.di.viewHistoryDomainModule
import ru.samtakoy.platform.di.platformModule
import ru.samtakoy.platform.importcards.di.importCardsFromZipPlatformModule
import ru.samtakoy.platform.notification.di.notificationPlatformModule
import ru.samtakoy.platform.permissions.di.permissionsPlatformAndroidModule
import ru.samtakoy.presentation.cards.di.cardsViewPresentationModule
import ru.samtakoy.presentation.main.di.mainScreenPresentationModule
import ru.samtakoy.presentation.qpacks.di.qPackPresentationModule
import ru.samtakoy.presentation.settings.di.settingsPresentationModule
import ru.samtakoy.presentation.themes.di.themesPresentationModule

fun koinOtherModulesModule() = module {
    includes(
        commonUtilsModule(),

        dataModule(),
        taskStateDataModule(),
        importDataModule(),

        cardDomainModule(),
        favoritesDomainModule(),
        learnCourseDomainModule(),
        qPackDomainModule(),
        themeDomainModule(),
        viewHistoryDomainModule(),
        importCardsDomainModule(),
        platformModule(),

        mainScreenPresentationModule(),
        qPackPresentationModule(),
        themesPresentationModule(),
        coursesPresentationModule(),
        favoritesPresentationModule(),
        schedulePresentationModule(),
        settingsPresentationModule(),
        cardsViewPresentationModule(),

        // platform dependend:
        importCardsFromZipPlatformModule(),
        notificationPlatformModule(),
        permissionsPlatformAndroidModule()
    )
}