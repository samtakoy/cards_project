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
public class CoursesModule {

    //private CurrentCourseHolder mCurrentCourseHolder;

    public CoursesModule(/*CurrentCourseHolder currentCourseHolder/**/) {
        //mCurrentCourseHolder = currentCourseHolder;
    }


    @Provides
    @Singleton
    CoursesRepository provideCoursesRepository(Context context){
        return new CoursesRepositoryImpl(context);
    }

    @Provides
    @Singleton
    NCoursesInteractor provideCoursesInteractor(Context context, CardsRepository cardsRepository, CoursesRepository corsesRepository){
        return new NCoursesInteractorImpl(context, cardsRepository, corsesRepository);
    }

    @Provides
    @Singleton
    CoursesPlanner provideCoursesPlanner(Context context){
        return new CoursesPlannerImpl(context);
    }

    /*
    @Provides
    @Singleton
    CurrentCourseHolder provideCurrentCourseHolder(){
        return mCurrentCourseHolder;
    }
    /***/

}
