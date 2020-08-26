package ru.samtakoy.core.business.di;


import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.QPacksRepository;
import ru.samtakoy.core.business.TagsRepository;
import ru.samtakoy.core.business.ThemesRepository;
import ru.samtakoy.core.business.impl.CardsInteractorImpl;
import ru.samtakoy.core.business.impl.CardsRepositoryImpl;
import ru.samtakoy.core.business.impl.QPacksRepositoryImpl;
import ru.samtakoy.core.business.impl.TagsRepositoryImpl;
import ru.samtakoy.core.business.impl.ThemesRepositoryImpl;
import ru.samtakoy.core.database.room.MyRoomDb;
import ru.samtakoy.core.di.modules.AppModule;

@Module(includes = {AppModule.class})
public abstract class CardsModule {

    @Binds
    @Singleton
    abstract TagsRepository provideTagsRepository(TagsRepositoryImpl impl);

    @Provides
    @Singleton
    static CardsRepository provideCardsRepository(MyRoomDb db) {
        return new CardsRepositoryImpl(db);
    }

    @Provides
    @Singleton
    static ThemesRepository provideThemesRepository(MyRoomDb db) {
        return new ThemesRepositoryImpl(db);
    }

    @Provides
    @Singleton
    static QPacksRepository provideQPacksRepository(MyRoomDb db) {
        return new QPacksRepositoryImpl(db);
    }

    @Binds
    @Singleton
    abstract CardsInteractor bindsCardsInteractor(CardsInteractorImpl impl);

    /*
    static CardsInteractor provideCardsInteractor(
            Context context, CardsRepository cardsRep, ThemesRepository themesRep,QPacksRepository qPacksRep
    ) {
        return new CardsInteractorImpl(
                context, cardsRep, themesRep, qPacksRep
        );
    }*/


}
