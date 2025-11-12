package ru.samtakoy.core

import android.content.Context
import android.os.Process
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import io.github.aakira.napier.Napier
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.samtakoy.BuildConfig
import ru.samtakoy.common.utils.log.CustomLogger
import ru.samtakoy.common.utils.log.MyLog
import ru.samtakoy.core.app.di.koinAppModule
import com.example.maindi.koinModulesModule

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
            val logger: CustomLogger by inject()
            Napier.base(logger)
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
                koinModulesModule()
            )
        }
    }
}
