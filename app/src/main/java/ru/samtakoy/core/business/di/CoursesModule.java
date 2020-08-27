package ru.samtakoy.core.business.di;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.samtakoy.core.api.di.ApiModule;
import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.business.TempCourseRepository;
import ru.samtakoy.core.business.impl.CoursesPlannerImpl;
import ru.samtakoy.core.business.impl.CoursesRepositoryImpl;
import ru.samtakoy.core.business.impl.NCoursesInteractorImpl;
import ru.samtakoy.core.business.impl.TempCourseRepositoryImpl;
import ru.samtakoy.core.database.room.di.DatabaseModule;
import ru.samtakoy.core.di.modules.AppModule;

@Module(includes = {AppModule.class, ApiModule.class, CardsModule.class, DatabaseModule.class})
public abstract class CoursesModule {


    @Binds
    @Singleton
    abstract TempCourseRepository provideTempCourseRepository(TempCourseRepositoryImpl impl);

    @Binds
    @Singleton
    abstract CoursesRepository provideCoursesRepository(CoursesRepositoryImpl impl);

    @Binds
    @Singleton
    abstract NCoursesInteractor provideCoursesInteractor(NCoursesInteractorImpl impl);


    @Binds
    @Singleton
    abstract CoursesPlanner provideCoursesPlanner(CoursesPlannerImpl impl);


}
