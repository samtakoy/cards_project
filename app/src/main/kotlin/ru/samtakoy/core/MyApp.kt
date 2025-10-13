package ru.samtakoy.core

import android.os.Process
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import ru.samtakoy.BuildConfig
import ru.samtakoy.common.di.DaggerCommonUtilsComponent
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.app.di.components.DaggerAppComponent
import ru.samtakoy.platform.di.PlatformModule
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.data.di.DaggerDataModuleComponent
import ru.samtakoy.platform.di.DaggerPlatformComponent
import ru.samtakoy.platform.di.PlatformComponent
import timber.log.Timber

class MyApp : MultiDexApplication() {
    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        MyLog.add("MyApp.onCreate(), " + Process.myPid())


        Stetho.initializeWithDefaults(this)

        val platformModule = PlatformModule(this)

        val platformComponent = DaggerPlatformComponent.builder()
            .platformModule(platformModule)
            .build()

        val commonUtilsComponent = DaggerCommonUtilsComponent.builder()
            .platformComponent(platformComponent)
            .build()

        val dataModuleComponent = DaggerDataModuleComponent.builder()
            .platformComponent(platformComponent)
            .commonUtilsComponent(commonUtilsComponent)
            .build()

        Di.appComponent = DaggerAppComponent.builder()
            .dataModuleComponent(dataModuleComponent)
            .commonUtilsComponent(commonUtilsComponent)
            .platformModule(platformModule)
            .build()
        MyLog.add("DI init ok")

        try {
            // TODO
            //deleteDatabase(MyDb.DB_NAME);
        } catch (ignore: Throwable) {
        }
    }

    companion object {
        private const val TAG = "MyApp"
    }
}
