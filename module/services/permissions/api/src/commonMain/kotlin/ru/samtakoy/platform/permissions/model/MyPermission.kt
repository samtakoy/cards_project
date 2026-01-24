package ru.samtakoy.platform.permissions.model

sealed interface MyPermission {
    data object Notifications: MyPermission
}