package ru.samtakoy.core;

import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

import ru.samtakoy.core.di.components.AppComponent;
import ru.samtakoy.core.di.components.DaggerAppComponent;
import ru.samtakoy.core.di.modules.AppModule;
import ru.samtakoy.core.screens.log.MyLog;
import ru.samtakoy.core.services.AppPreferences;

//public class MyApp extends Application {
public class MyApp extends MultiDexApplication {


    private static MyApp sInstance;
    public static MyApp getInstance(){ return sInstance; }

    private static final String TAG = "MyApp";


    private AppPreferences mPreferences;

    private AppComponent mAppComponent;

    @Override
    public void onTerminate() {

        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MyLog.add("MyApp.onCreate(), "+android.os.Process.myPid());

        sInstance = this;

        Stetho.initializeWithDefaults(this);
        mPreferences = new AppPreferences(this);


        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        try {

            // TODO
            // TODO
            // TODO
            // TODO
            //deleteDatabase(MyDb.DB_NAME);
        }catch(Throwable ignore){}
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public AppPreferences getPreferences(){
        return mPreferences;
    }


}
