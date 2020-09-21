package ru.samtakoy.core.app.di.modules;


import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.samtakoy.core.data.local.database.room.MyRoomDb;
import ru.samtakoy.core.data.local.reps.CardsRepositoryImpl;
import ru.samtakoy.core.data.local.reps.QPacksRepositoryImpl;
import ru.samtakoy.core.data.local.reps.TagsRepositoryImpl;
import ru.samtakoy.core.data.local.reps.ThemesRepositoryImpl;
import ru.samtakoy.core.domain.CardsInteractor;
import ru.samtakoy.core.domain.CardsRepository;
import ru.samtakoy.core.domain.QPacksRepository;
import ru.samtakoy.core.domain.TagsRepository;
import ru.samtakoy.core.domain.ThemesRepository;
import ru.samtakoy.core.domain.impl.CardsInteractorImpl;

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
