package ru.samtakoy.core

import android.content.Context
import android.os.Process
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.samtakoy.BuildConfig
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.core.app.di.koinAppModule
import ru.samtakoy.core.app.di.koinOtherModulesModule
import timber.log.Timber

class MyApp : MultiDexApplication() {
    override fun onTerminate() {
        super.onTerminate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        startKoinDi(this)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        MyLog.add("MyApp.onCreate(), " + Process.myPid())
        Stetho.initializeWithDefaults(this)

        try {
            // TODO
            //deleteDatabase(MyDb.DB_NAME);
        } catch (ignore: Throwable) {
        }
    }

    private fun startKoinDi(androidContext: Context) {
        startKoin {
            androidContext(androidContext)
            modules(
                // Сгенерированные Koin модули
                //org.koin.ksp.generated.module
                koinAppModule(),
                koinOtherModulesModule()
            )
        }
    }

    companion object {
        private const val TAG = "MyApp"
    }
}
