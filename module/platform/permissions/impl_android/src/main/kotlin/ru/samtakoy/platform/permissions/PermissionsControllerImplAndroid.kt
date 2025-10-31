package ru.samtakoy.platform.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import ru.samtakoy.platform.permissions.mapper.AndroidPlatformPermissionsMapper
import ru.samtakoy.platform.permissions.model.MyPermission
import ru.samtakoy.platform.permissions.model.PermissionDelegate
import ru.samtakoy.platform.permissions.model.PermissionRequestResult

/**
 * Реализация скопирована в общих моментах из https://github.com/icerockdev/moko-permissions.git
 * */
class PermissionsControllerImplAndroid(
    private val appContext: Context,
    private val permissionsMapper: AndroidPlatformPermissionsMapper
) : PermissionsController {

    private val activityHolder = MutableStateFlow<Activity?>(null)

    private val mutex: Mutex = Mutex()

    private val launcherHolder = MutableStateFlow<ActivityResultLauncher<Array<String>>?>(null)

    private val permissionRequestResultFlow = MutableSharedFlow<PermissionRequestResult>(
        extraBufferCapacity = 1
    )

    private val delegates = mutableMapOf<MyPermission, PermissionDelegate>()

    fun bind(activity: AppCompatActivity) {
        unbind()

        this.activityHolder.value = activity
        val activityResultRegistryOwner = activity as ActivityResultRegistryOwner

        val launcher = activityResultRegistryOwner.activityResultRegistry.register(
            REQUEST_KEY,
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isCancelled = permissions.isEmpty()

            if (isCancelled) {
                permissionRequestResultFlow.tryEmit(PermissionRequestResult.CANCELLED)
                return@register
            }

            val success = permissions.values.all { it }

            if (success) {
                permissionRequestResultFlow.tryEmit(PermissionRequestResult.GRANTED)
            } else {
                if (shouldShowRequestPermissionRationale(activity, permissions.keys.first())) {
                    permissionRequestResultFlow.tryEmit(PermissionRequestResult.DENIED)
                } else {
                    permissionRequestResultFlow.tryEmit(PermissionRequestResult.DENIED_ALWAYS)
                }
            }
        }

        launcherHolder.value = launcher

        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    unbind()
                    source.lifecycle.removeObserver(this)
                }
            }
        }
        activity.lifecycle.addObserver(observer)
    }

    fun unbind() {
        launcherHolder.value?.unregister()
        activityHolder.value = null
        launcherHolder.value = null
    }

    override suspend fun requestPermission(permission: MyPermission): PermissionState {
        mutex.withLock {
            val launcher = awaitActivityResultLauncher()
            val platformPermission = getPlatformPermissions(permission)

            if (platformPermission.isEmpty()) {
                // Запрос не нужен
                permissionRequestResultFlow.tryEmit(PermissionRequestResult.GRANTED)
            } else {
                launcher.launch(platformPermission.toTypedArray())
            }

            return when (permissionRequestResultFlow.first()) {
                PermissionRequestResult.GRANTED -> PermissionState.Granted
                PermissionRequestResult.DENIED -> PermissionState.Denied
                PermissionRequestResult.DENIED_ALWAYS -> PermissionState.DeniedAlways
                PermissionRequestResult.CANCELLED -> PermissionState.NotGranted
            }
        }
    }

    override suspend fun isPermissionGranted(permission: MyPermission): Boolean {
        return getPermissionState(permission) == PermissionState.Granted
    }

    override suspend fun getPermissionState(permission: MyPermission): PermissionState {
        val delegate = getPermissionStateDelegate(permission)
        delegate.getPermissionStateOverride(appContext)?.let { return it }
        val platformPermissions: List<String> = getPlatformPermissions(permission)
        val status: List<Int> = platformPermissions.map {
            ContextCompat.checkSelfPermission(appContext, it)
        }
        val isAllGranted: Boolean = status.all { it == PackageManager.PERMISSION_GRANTED }
        if (isAllGranted) return PermissionState.Granted

        val isAllRequestRationale: Boolean = platformPermissions.all {
            shouldShowRequestPermissionRationale(it)
        }
        return if (isAllRequestRationale) {
            PermissionState.Denied
        } else {
            PermissionState.NotGranted
        }
    }

    override fun openAppSettings() {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", appContext.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        appContext.startActivity(intent)
    }

    private suspend fun awaitActivityResultLauncher(): ActivityResultLauncher<Array<String>> {
        val activityResultLauncher = launcherHolder.value
        if (activityResultLauncher != null) return activityResultLauncher

        return withTimeoutOrNull(AWAIT_ACTIVITY_TIMEOUT_DURATION_MS) {
            launcherHolder.filterNotNull().first()
        } ?: error(
            "activityResultLauncher is null, `bind` function was never called"
        )
    }

    private suspend fun awaitActivity(): Activity {
        val activity = activityHolder.value
        if (activity != null) return activity

        return withTimeoutOrNull(AWAIT_ACTIVITY_TIMEOUT_DURATION_MS) {
            activityHolder.filterNotNull().first()
        } ?: error(
            "activity is null, `bind` function was never called"
        )
    }

    private suspend fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        val activity = awaitActivity()
        return shouldShowRequestPermissionRationale(activity, permission)
    }

    private fun shouldShowRequestPermissionRationale(
        activity: Activity,
        permission: String
    ): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            permission
        )
    }

    private fun getPermissionStateDelegate(permission: MyPermission): PermissionDelegate {
        return delegates.getOrPut(permission) {
            permissionsMapper.mapToStateDelegate(permission)
        }
    }

    private fun getPlatformPermissions(permission: MyPermission): List<String> {
        return permissionsMapper.mapToPlatformPermissions(permission)
    }

    private companion object {
        private const val AWAIT_ACTIVITY_TIMEOUT_DURATION_MS = 2000L
        private val REQUEST_KEY = "PermissionsControllerImplAndroid.REQUEST_KEY"
    }
}
