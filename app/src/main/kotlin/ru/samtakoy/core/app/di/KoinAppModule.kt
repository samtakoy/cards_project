package ru.samtakoy.core.app.di

import org.koin.dsl.module
import ru.samtakoy.core.app.di.modules.apiDataModule
import ru.samtakoy.core.app.di.modules.dialogPresentationModule
import ru.samtakoy.core.presentation.favorites.di.favoritesPresentationModule
import ru.samtakoy.core.presentation.cards.di.cardsViewPresentationModule
import ru.samtakoy.core.presentation.qpack.di.qPackPresentationModule
import ru.samtakoy.core.presentation.themes.di.themesPresentationModule
import ru.samtakoy.features.import_export.di.exportModule
import ru.samtakoy.features.import_export.di.importModule
import ru.samtakoy.features.notifications.di.notificationsDomainModule
import ru.samtakoy.features.views.di.viewsFeatureModule

fun koinAppModule() = module {
    includes(
        apiDataModule(),

        notificationsDomainModule(),

        viewsFeatureModule(),
        themesPresentationModule(),
        qPackPresentationModule(),
        cardsViewPresentationModule(),
        dialogPresentationModule(),

        importModule(),
        exportModule(),
    )
}