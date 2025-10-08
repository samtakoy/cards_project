package ru.samtakoy.core.app.di.modules

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.some.ResourcesImpl
import javax.inject.Singleton

@Module
class AppModule(
    private val mContext: Context
) {
    @Provides @Singleton fun provideContext(): Context {
        return mContext
    }

    @Provides @Singleton fun provideContentResolver(): ContentResolver {
        return mContext.getContentResolver()
    }

    @Provides @Singleton fun provideResources(resources: ResourcesImpl): Resources {
        return resources
    }
}
