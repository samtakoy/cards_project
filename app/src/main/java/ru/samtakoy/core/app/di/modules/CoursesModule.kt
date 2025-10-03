package ru.samtakoy.core.app.di.modules;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.samtakoy.core.data.local.reps.CoursesRepositoryImpl;
import ru.samtakoy.core.data.local.reps.TempCourseRepositoryImpl;
import ru.samtakoy.core.domain.CoursesPlanner;
import ru.samtakoy.core.domain.CoursesRepository;
import ru.samtakoy.core.domain.NCoursesInteractor;
import ru.samtakoy.core.domain.TempCourseRepository;
import ru.samtakoy.core.domain.impl.CoursesPlannerImpl;
import ru.samtakoy.core.domain.impl.NCoursesInteractorImpl;

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
