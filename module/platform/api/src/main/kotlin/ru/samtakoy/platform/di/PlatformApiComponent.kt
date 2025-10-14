package ru.samtakoy.platform.di

import android.content.ContentResolver
import android.content.Context

interface PlatformApiComponent {
    fun context(): Context
    fun contentResolver(): ContentResolver
}