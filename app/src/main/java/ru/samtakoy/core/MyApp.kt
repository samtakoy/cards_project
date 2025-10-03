package ru.samtakoy.core

import android.os.Process
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import ru.samtakoy.BuildConfig
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.app.di.components.DaggerAppComponent
import ru.samtakoy.core.app.di.modules.AppModule
import ru.samtakoy.core.presentation.log.MyLog
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

        Di.appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
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
