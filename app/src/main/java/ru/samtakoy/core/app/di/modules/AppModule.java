package ru.samtakoy.core.app.di.modules;

import android.content.ContentResolver;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.samtakoy.core.app.some.Resources;
import ru.samtakoy.core.app.some.ResourcesImpl;


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

    @Provides
    @Singleton
    public Resources provideResources(ResourcesImpl resources) {
        return resources;
    }
}
