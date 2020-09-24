package ru.samtakoy.core;

import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

import ru.samtakoy.core.app.di.Di;
import ru.samtakoy.core.app.di.components.DaggerAppComponent;
import ru.samtakoy.core.app.di.modules.AppModule;
import ru.samtakoy.core.presentation.log.MyLog;


public class MyApp extends MultiDexApplication {


    private static final String TAG = "MyApp";


    @Override
    public void onTerminate() {

        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MyLog.add("MyApp.onCreate(), "+android.os.Process.myPid());


        Stetho.initializeWithDefaults(this);

        Di.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        try {
            // TODO
            //deleteDatabase(MyDb.DB_NAME);
        }catch(Throwable ignore){}
    }


}
