package ru.samtakoy.platform.permissions.model

import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import ru.samtakoy.platform.permissions.PermissionState

internal class NotificationsPermissionDelegate : PermissionDelegate {
    override fun getPermissionStateOverride(applicationContext: Context): PermissionState? {
        if (Build.VERSION.SDK_INT !in VERSIONS_WITHOUT_NOTIFICATION_PERMISSION) return null

        val isNotificationsEnabled = NotificationManagerCompat.from(applicationContext)
            .areNotificationsEnabled()
        return if (isNotificationsEnabled) {
            PermissionState.Granted
        } else {
            PermissionState.DeniedAlways
        }
    }
}

private val VERSIONS_WITHOUT_NOTIFICATION_PERMISSION =
    Build.VERSION_CODES.KITKAT until Build.VERSION_CODES.TIRAMISU