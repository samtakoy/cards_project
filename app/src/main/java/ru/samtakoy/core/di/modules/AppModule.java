package ru.samtakoy.core.di.modules;

import android.content.ContentResolver;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    public ContentResolver provideContentResolver() {
        return mContext.getContentResolver();
    }

    /*
    @Provides
    @Singleton
    public MyRoomDb getDataBase()  {
        return Room.databaseBuilder(mContext, MyRoomDb.class, DB_NAME).build();
    }*/

}
