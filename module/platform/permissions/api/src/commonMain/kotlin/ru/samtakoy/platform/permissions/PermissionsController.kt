package ru.samtakoy.platform.permissions

import ru.samtakoy.platform.permissions.model.MyPermission

interface PermissionsController {
    suspend fun requestPermission(permission: MyPermission): PermissionState
    suspend fun isPermissionGranted(permission: MyPermission): Boolean
    suspend fun getPermissionState(permission: MyPermission): PermissionState
    fun openAppSettings()
}