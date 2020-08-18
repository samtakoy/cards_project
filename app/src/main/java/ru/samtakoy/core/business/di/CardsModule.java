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
public abstract class CardsModule {

    @Provides
    @Singleton
    static CardsRepository provideCardsRepository(Context context, EventBus eventBus) {
        return new CardsRepositoryImpl(context, eventBus);
    }

    @Provides
    @Singleton
    static CardsInteractor provideCardsInteractor(Context context, CardsRepository cardsRepository) {
        return new CardsInteractorImpl(context, cardsRepository);
    }


}
