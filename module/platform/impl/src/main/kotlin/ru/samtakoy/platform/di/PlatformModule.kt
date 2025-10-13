package ru.samtakoy.platform.di

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class PlatformModule(
    private val mContext: Context
) {
    @Provides fun provideContext(): Context {
        return mContext
    }

    @Provides fun provideContentResolver(): ContentResolver {
        return mContext.getContentResolver()
    }


}