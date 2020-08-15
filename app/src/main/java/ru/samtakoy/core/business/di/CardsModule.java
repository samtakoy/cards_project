package ru.samtakoy.core.business.di;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.impl.CardsInteractorImpl;
import ru.samtakoy.core.business.impl.CardsRepositoryImpl;
import ru.samtakoy.core.di.modules.AppModule;

@Module(includes = {AppModule.class, EventBusModule.class})
public class CardsModule {

    @Provides
    @Singleton
    CardsRepository provideCardsRepository(Context context, EventBus eventBus){
        return new CardsRepositoryImpl(context, eventBus);
    }

    @Provides
    @Singleton
    CardsInteractor provideCardsInteractor(Context context, CardsRepository cardsRepository){
        return new CardsInteractorImpl(context, cardsRepository);
    }


}
