package ru.samtakoy.core.app.di

import org.koin.dsl.module
import ru.samtakoy.common.di.oldResourcesModule
import ru.samtakoy.core.app.di.modules.apiDataModule
import ru.samtakoy.core.app.di.modules.dialogPresentationModule
import ru.samtakoy.core.app.di.modules.workerFactoryModule
import ru.samtakoy.core.presentation.courses.di.coursesPresentationModule
import ru.samtakoy.core.presentation.favorites.di.favoritesPresentationModule
import ru.samtakoy.core.presentation.qpack.di.qPackPresentationModuleFragment
import ru.samtakoy.core.presentation.schedule.di.schedulePresentationModule
import ru.samtakoy.features.import_export.di.exportModule
import ru.samtakoy.features.import_export.di.importModule
import ru.samtakoy.features.notifications.di.notificationsDomainModule
import ru.samtakoy.features.views.di.viewsFeatureModule

fun koinAppModule() = module {
    includes(
        oldResourcesModule(),

        apiDataModule(),
        workerFactoryModule(),

        notificationsDomainModule(),

        viewsFeatureModule(),
        qPackPresentationModuleFragment(),
        dialogPresentationModule(),
        schedulePresentationModule(),
        coursesPresentationModule(),
        favoritesPresentationModule(),

        importModule(),
        exportModule(),
    )
}