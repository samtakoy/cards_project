package ru.samtakoy.core.business.di;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.samtakoy.core.di.modules.AppModule;

@Module()
public class EventBusModule {

    @Provides
    @Singleton
    EventBus provideEventBus(){
        return EventBus.getDefault();
    }


}
