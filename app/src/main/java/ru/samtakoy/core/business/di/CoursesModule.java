package ru.samtakoy.core.business.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.business.impl.CoursesPlannerImpl;
import ru.samtakoy.core.business.impl.CoursesRepositoryImpl;
import ru.samtakoy.core.business.impl.NCoursesInteractorImpl;
import ru.samtakoy.core.di.modules.AppModule;

@Module(includes = {AppModule.class, CardsModule.class})
public abstract class CoursesModule {


    @Provides
    @Singleton
    static CoursesRepository provideCoursesRepository(Context context) {
        return new CoursesRepositoryImpl(context);
    }

    @Provides
    @Singleton
    static NCoursesInteractor provideCoursesInteractor(Context context, CardsRepository cardsRepository, CoursesRepository corsesRepository) {
        return new NCoursesInteractorImpl(context, cardsRepository, corsesRepository);
    }

    @Provides
    @Singleton
    static CoursesPlanner provideCoursesPlanner(Context context) {
        return new CoursesPlannerImpl(context);
    }


}
