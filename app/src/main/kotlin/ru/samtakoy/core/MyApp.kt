package ru.samtakoy.core

import android.content.Context
import android.os.Process
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import androidx.work.WorkManager
import com.facebook.stetho.Stetho
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin
import ru.samtakoy.BuildConfig
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.core.app.di.koinAppModule
import ru.samtakoy.core.app.di.koinOtherModulesModule
import timber.log.Timber

class MyApp : MultiDexApplication()/*, Configuration.Provider*/ {
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

    /*
    override val workManagerConfiguration: Configuration
        get() {
            return Configuration.Builder()
                .setWorkerFactory(getKoin().get<KoinWorkerFactory>())
                .build()
        }*/

    private fun startKoinDi(androidContext: Context) {
        startKoin {
            androidContext(androidContext)
            // workManagerFactory()
            modules(
                koinAppModule(),
                koinOtherModulesModule()
            )
        }
    }
}
