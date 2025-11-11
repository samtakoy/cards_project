package ru.samtakoy.platform.permissions.model

import android.content.Context
import ru.samtakoy.platform.permissions.PermissionState

interface PermissionDelegate {
    fun getPermissionStateOverride(applicationContext: Context): PermissionState?
}