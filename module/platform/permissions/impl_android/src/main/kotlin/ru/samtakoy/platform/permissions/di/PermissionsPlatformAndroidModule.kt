package ru.samtakoy.platform.permissions.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.platform.permissions.mapper.AndroidPlatformPermissionsMapper
import ru.samtakoy.platform.permissions.mapper.AndroidPlatformPermissionsMapperImpl
import ru.samtakoy.platform.permissions.PermissionsController
import ru.samtakoy.platform.permissions.PermissionsControllerImplAndroid

fun permissionsPlatformAndroidModule() = module {
    factoryOf(::AndroidPlatformPermissionsMapperImpl) bind AndroidPlatformPermissionsMapper::class
    singleOf(::PermissionsControllerImplAndroid) bind PermissionsController::class
}