package ru.samtakoy.core.business.di;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module()
public abstract class EventBusModule {

    @Provides
    @Singleton
    public static EventBus provideEventBus() {
        return EventBus.getDefault();
    }


}
