package com.example.maindi

import org.koin.dsl.module
import ru.samtakoy.common.utils.di.commonUtilsModule
import ru.samtakoy.data.di.dataModule
import ru.samtakoy.importcards.data.di.importDataModule
import ru.samtakoy.speech.data.di.speechDataModule
import ru.samtakoy.data.task.di.taskStateDataModule
import ru.samtakoy.domain.di.cardDomainModule
import ru.samtakoy.domain.favorites.di.favoritesDomainModule
import ru.samtakoy.importcards.domain.di.importCardsDomainModule
import ru.samtakoy.domain.learncourse.di.learnCourseDomainModule
import ru.samtakoy.domain.qpack.di.qPackDomainModule
import ru.samtakoy.speech.domain.di.speechDomainModule
import ru.samtakoy.domain.theme.di.themeDomainModule
import ru.samtakoy.domain.view.di.viewHistoryDomainModule
import ru.samtakoy.importcards.platform.di.importCardsFromZipPlatformModule
import ru.samtakoy.platform.notification.di.notificationPlatformModule
import ru.samtakoy.platform.permissions.di.permissionsPlatformModule
import ru.samtakoy.presentation.cards.di.cardsViewPresentationModule
import ru.samtakoy.presentation.main.di.mainScreenPresentationModule
import ru.samtakoy.presentation.qpacks.di.qPackPresentationModule
import ru.samtakoy.presentation.settings.di.settingsPresentationModule
import ru.samtakoy.presentation.themes.di.themesPresentationModule
import ru.samtakoy.speech.platform.di.speechPlatformModule

fun koinModulesModule() = module {
    includes(
        commonUtilsModule(),

        dataModule(),
        taskStateDataModule(),
        importDataModule(),
        speechDataModule(),

        cardDomainModule(),
        favoritesDomainModule(),
        learnCourseDomainModule(),
        qPackDomainModule(),
        themeDomainModule(),
        viewHistoryDomainModule(),
        importCardsDomainModule(),
        speechDomainModule(),

        mainScreenPresentationModule(),
        cardsViewPresentationModule(),
        qPackPresentationModule(),
        themesPresentationModule(),
        settingsPresentationModule(),

        // platform dependend:
        importCardsFromZipPlatformModule(),
        notificationPlatformModule(),
        permissionsPlatformModule(),
        speechPlatformModule()
    )
}