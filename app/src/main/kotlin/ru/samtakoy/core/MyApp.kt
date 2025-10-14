package ru.samtakoy.core

import android.os.Process
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import ru.samtakoy.BuildConfig
import ru.samtakoy.common.di.DaggerCommonUtilsComponent
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.app.di.components.DaggerAppComponent
import ru.samtakoy.data.di.DaggerDataModuleImplComponent
import ru.samtakoy.domain.di.DaggerCardDomainImplComponent
import ru.samtakoy.domain.learncourse.di.DaggerLearnCourseDomainImplComponent
import ru.samtakoy.domain.qpack.di.DaggerQPackDomainImplComponent
import ru.samtakoy.domain.view.di.DaggerViewHistoryDomainImplComponent
import ru.samtakoy.platform.di.DaggerPlatformImplComponent
import ru.samtakoy.platform.di.PlatformModule
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

        val platformComponent = DaggerPlatformImplComponent.builder()
            .platformModule(platformModule)
            .build()

        val commonUtilsComponent = DaggerCommonUtilsComponent.builder()
            .platformApiComponent(platformComponent)
            .build()

        val dataModuleComponent = DaggerDataModuleImplComponent.builder()
            .platformApiComponent(platformComponent)
            .commonUtilsComponent(commonUtilsComponent)
            .build()

        val cardDomainComponent = DaggerCardDomainImplComponent.builder()
            .dataModuleApiComponent(dataModuleComponent)
            .build()

        val qPackDomainComponent = DaggerQPackDomainImplComponent.builder()
            .dataModuleApiComponent(dataModuleComponent)
            .build()

        val viewHistoryDomainComponent = DaggerViewHistoryDomainImplComponent.builder()
            .dataModuleApiComponent(dataModuleComponent)
            .cardDomainApiComponent(cardDomainComponent)
            .build()

        val learnCourseDomainComponent = DaggerLearnCourseDomainImplComponent.builder()
            .dataModuleApiComponent(dataModuleComponent)
            .qPackDomainApiComponent(qPackDomainComponent)
            .viewHistoryDomainApiComponent(viewHistoryDomainComponent)
            .platformApiComponent(platformComponent)
            .build()

        Di.appComponent = DaggerAppComponent.builder()
            .dataModuleApiComponent(dataModuleComponent)
            .commonUtilsComponent(commonUtilsComponent)
            .cardDomainApiComponent(cardDomainComponent)
            .qPackDomainApiComponent(qPackDomainComponent)
            .learnCourseDomainApiComponent(learnCourseDomainComponent)
            .viewHistoryDomainApiComponent(viewHistoryDomainComponent)
            // ?
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
