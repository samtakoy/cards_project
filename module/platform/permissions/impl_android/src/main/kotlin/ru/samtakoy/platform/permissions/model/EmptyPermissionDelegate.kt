package ru.samtakoy.platform.permissions.model

import android.content.Context
import ru.samtakoy.platform.permissions.PermissionState

internal object EmptyPermissionDelegate : PermissionDelegate {
    override fun getPermissionStateOverride(applicationContext: Context): PermissionState? = null
}