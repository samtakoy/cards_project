package ru.samtakoy.platform.permissions

import ru.samtakoy.platform.permissions.model.MyPermission

internal class PermissionsControllerImpl : PermissionsController {
    override suspend fun requestPermission(permission: MyPermission): PermissionState {
        return PermissionState.Granted
    }

    override suspend fun isPermissionGranted(permission: MyPermission): Boolean {
        return true
    }

    override suspend fun getPermissionState(permission: MyPermission): PermissionState {
        return PermissionState.Granted
    }

    override fun openAppSettings() = Unit
}