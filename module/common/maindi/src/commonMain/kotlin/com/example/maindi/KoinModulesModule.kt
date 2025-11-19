package com.example.maindi

import org.koin.dsl.module
import ru.samtakoy.common.utils.di.commonUtilsModule
import ru.samtakoy.data.di.dataModule
import ru.samtakoy.data.importcards.di.importDataModule
import ru.samtakoy.data.speech.di.speechDataModule
import ru.samtakoy.data.speech.di.speechPlatformDataModule
import ru.samtakoy.data.task.di.taskStateDataModule
import ru.samtakoy.domain.di.cardDomainModule
import ru.samtakoy.domain.favorites.di.favoritesDomainModule
import ru.samtakoy.domain.importcards.di.importCardsDomainModule
import ru.samtakoy.domain.learncourse.di.learnCourseDomainModule
import ru.samtakoy.domain.qpack.di.qPackDomainModule
import ru.samtakoy.domain.speech.di.speechDomainModule
import ru.samtakoy.domain.theme.di.themeDomainModule
import ru.samtakoy.domain.view.di.viewHistoryDomainModule
import ru.samtakoy.platform.importcards.di.importCardsFromZipPlatformModule
import ru.samtakoy.platform.notification.di.notificationPlatformModule
import ru.samtakoy.platform.permissions.di.permissionsPlatformModule
import ru.samtakoy.presentation.cards.di.cardsViewPresentationModule
import ru.samtakoy.presentation.main.di.mainScreenPresentationModule
import ru.samtakoy.presentation.qpacks.di.qPackPresentationModule
import ru.samtakoy.presentation.settings.di.settingsPresentationModule
import ru.samtakoy.presentation.themes.di.themesPresentationModule
import ru.samtakoy.platform.speech.di.speechPlatformModule

fun koinModulesModule() = module {
    includes(
        commonUtilsModule(),

        dataModule(),
        taskStateDataModule(),
        importDataModule(),
        speechDataModule(),
        speechPlatformDataModule(),

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