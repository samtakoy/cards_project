package ru.samtakoy.platform.notification.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.samtakoy.platform.notification.AndroidNotificationRepositoryImpl

actual fun notificationPlatformModule() = module {
    factoryOf(::AndroidNotificationRepositoryImpl)
}