package com.example.maindi

import org.koin.dsl.module
import ru.samtakoy.common.utils.di.commonUtilsModule
import ru.samtakoy.core.log.di.coreLogModule
import ru.samtakoy.data.di.coreDbModule
import ru.samtakoy.data.remote.di.coreNetworkModule
import ru.samtakoy.importcards.data.di.importCardsDataModule
import ru.samtakoy.speech.data.di.speechDataModule
import ru.samtakoy.data.task.di.taskStateFeatureModule
import ru.samtakoy.domain.di.cardFeatureModule
import ru.samtakoy.domain.favorites.di.favoritesFeatureModule
import ru.samtakoy.importcards.domain.di.importCardsFeatureModule
import ru.samtakoy.domain.learncourse.di.learnCourseFeatureModule
import ru.samtakoy.domain.qpack.di.qPackFeatureModule
import ru.samtakoy.speech.domain.di.speechDomainModule
import ru.samtakoy.domain.theme.di.themeFeatureModule
import ru.samtakoy.domain.view.di.viewHistoryFeatureModule
import ru.samtakoy.download.di.downloadFeatureModule
import ru.samtakoy.importcards.platform.di.importCardsFromZipPlatformModule
import ru.samtakoy.platform.notification.di.notificationUtilFeatureModule
import ru.samtakoy.platform.permissions.di.permissionsUtilFeatureModule
import ru.samtakoy.presentation.cards.di.cardPresentationModule
import ru.samtakoy.presentation.main.di.mainScreenModule
import ru.samtakoy.presentation.qpacks.di.qPackPresentationModule
import ru.samtakoy.presentation.settings.di.settingsPresentationModule
import ru.samtakoy.presentation.themes.di.themePresentationModule
import ru.samtakoy.speech.platform.di.speechPlatformModule
import ru.samtakoy.speech.presentation.di.speechPresentationModule
import ru.samtakoy.tabnavigation.di.tabNavigationModule

fun koinModulesModule() = module {
    includes(
        commonUtilsModule(),

        // core
        coreLogModule(),
        coreDbModule(),
        coreNetworkModule(),

        // main
        mainScreenModule(),
        tabNavigationModule(),

        // features
        speechDataModule(),
        speechDomainModule(),
        speechPresentationModule(),
        speechPlatformModule(),

        importCardsDataModule(),
        importCardsFeatureModule(),
        importCardsFromZipPlatformModule(),

        cardFeatureModule(),
        cardPresentationModule(),
        favoritesFeatureModule(),
        learnCourseFeatureModule(),
        qPackFeatureModule(),
        qPackPresentationModule(),
        themeFeatureModule(),
        themePresentationModule(),
        viewHistoryFeatureModule(),
        settingsPresentationModule(),

        // util-features
        notificationUtilFeatureModule(),
        permissionsUtilFeatureModule(),
        taskStateFeatureModule(),
        downloadFeatureModule()

    )
}