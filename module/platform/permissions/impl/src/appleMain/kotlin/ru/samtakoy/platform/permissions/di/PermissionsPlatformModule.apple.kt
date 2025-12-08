package ru.samtakoy.platform.permissions.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.platform.permissions.PermissionsController
import ru.samtakoy.platform.permissions.PermissionsControllerImpl

actual fun permissionsPlatformModule(): Module = module {
    singleOf(::PermissionsControllerImpl) bind PermissionsController::class
}