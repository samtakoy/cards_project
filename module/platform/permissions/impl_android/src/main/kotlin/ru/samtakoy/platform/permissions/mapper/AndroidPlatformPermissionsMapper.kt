package ru.samtakoy.platform.permissions.mapper

import android.Manifest
import android.os.Build
import ru.samtakoy.platform.permissions.model.EmptyPermissionDelegate
import ru.samtakoy.platform.permissions.model.MyPermission
import ru.samtakoy.platform.permissions.model.NotificationsPermissionDelegate
import ru.samtakoy.platform.permissions.model.PermissionDelegate

interface AndroidPlatformPermissionsMapper {
    fun mapToPlatformPermissions(permission: MyPermission): List<String>
    fun mapToStateDelegate(permission: MyPermission): PermissionDelegate
}

internal class AndroidPlatformPermissionsMapperImpl : AndroidPlatformPermissionsMapper {
    override fun mapToPlatformPermissions(permission: MyPermission): List<String> {
        return when (permission) {
            MyPermission.Notifications -> {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    listOf(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    listOf("")
                }
            }
        }
    }

    override fun mapToStateDelegate(permission: MyPermission): PermissionDelegate {
        @Suppress("REDUNDANT_ELSE_IN_WHEN")
        return when (permission) {
            MyPermission.Notifications -> NotificationsPermissionDelegate()
            else -> EmptyPermissionDelegate
        }
    }
}