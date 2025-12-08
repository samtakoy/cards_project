package ru.samtakoy.core.app

import android.content.Context
import android.os.Process
import androidx.multidex.MultiDexApplication
import com.example.maindi.koinModulesModule
import com.facebook.stetho.Stetho
import io.github.aakira.napier.Napier
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import ru.samtakoy.BuildConfig
import ru.samtakoy.common.utils.log.CustomLogger
import ru.samtakoy.common.utils.log.MyLog

class MyApp : MultiDexApplication() {

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
    }

    private fun startKoinDi(androidContext: Context) {
        GlobalContext.startKoin {
            androidContext(androidContext)
            modules(
                // Больше не используется - старый код, 5-летней выдержки
                // koinAppModule(),
                koinModulesModule()
            )
        }
    }
}