package ru.samtakoy.platform.di

import android.content.ContentResolver
import android.content.Context
import dagger.Component

@Component(
    modules = [PlatformModule::class]
)
interface PlatformComponent {
    fun context(): Context
    fun contentResolver(): ContentResolver
}
