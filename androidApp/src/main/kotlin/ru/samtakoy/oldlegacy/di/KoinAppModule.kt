package ru.samtakoy.oldlegacy.di

import org.koin.dsl.module
import ru.samtakoy.common.di.oldResourcesModule
import ru.samtakoy.oldlegacy.di.modules.apiDataModule
import ru.samtakoy.oldlegacy.di.modules.dialogPresentationModule
import ru.samtakoy.oldlegacy.di.modules.workerFactoryModule
import ru.samtakoy.oldlegacy.core.presentation.courses.di.coursesPresentationModule
import ru.samtakoy.oldlegacy.core.presentation.favorites.di.favoritesPresentationModule
import ru.samtakoy.oldlegacy.core.presentation.qpack.di.qPackPresentationModuleFragment
import ru.samtakoy.oldlegacy.core.presentation.schedule.di.schedulePresentationModule
import ru.samtakoy.oldlegacy.features.import_export.di.exportModule
import ru.samtakoy.oldlegacy.features.import_export.di.importModule
import ru.samtakoy.oldlegacy.features.notifications.di.notificationsDomainModule
import ru.samtakoy.oldlegacy.features.views.di.viewsFeatureModule

@Deprecated(
    "На данный момент не используется (как и все в oldlegacy)." +
        "Оставлено для переноса в kmp-модули + Compose и рефакторинга в будущем"
)
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