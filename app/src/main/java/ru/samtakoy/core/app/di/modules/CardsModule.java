package ru.samtakoy.core.app.di.modules;


import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.samtakoy.core.data.local.reps.impl.CardsRepositoryImpl;
import ru.samtakoy.core.data.local.reps.impl.QPacksRepositoryImpl;
import ru.samtakoy.core.data.local.reps.impl.TagsRepositoryImpl;
import ru.samtakoy.core.data.local.reps.impl.ThemesRepositoryImpl;
import ru.samtakoy.core.domain.CardsInteractor;
import ru.samtakoy.core.data.local.reps.CardsRepository;
import ru.samtakoy.core.domain.FavoritesInteractor;
import ru.samtakoy.core.data.local.reps.QPacksRepository;
import ru.samtakoy.core.data.local.reps.TagsRepository;
import ru.samtakoy.core.data.local.reps.ThemesRepository;
import ru.samtakoy.core.domain.impl.CardsInteractorImpl;
import ru.samtakoy.core.domain.impl.FavoritesInteractorImpl;

@Module
public abstract class CardsModule {

    @Binds
    @Singleton
    abstract TagsRepository bindsTagsRepository(TagsRepositoryImpl impl);

    @Binds
    @Singleton
    abstract CardsRepository bindsCardsRepository(CardsRepositoryImpl impl);

    @Binds
    @Singleton
    abstract ThemesRepository bindsThemesRepository(ThemesRepositoryImpl impl);

    @Binds
    @Singleton
    abstract QPacksRepository bindsQPacksRepository(QPacksRepositoryImpl impl);

    @Binds
    abstract CardsInteractor bindsCardsInteractor(CardsInteractorImpl impl);

    @Binds
    abstract FavoritesInteractor bindsFavoritesInteractor(FavoritesInteractorImpl impl);

    // Просмотр
}
