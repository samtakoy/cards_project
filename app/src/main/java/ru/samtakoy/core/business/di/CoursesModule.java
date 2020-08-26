package ru.samtakoy.core.business.di;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.business.impl.CoursesPlannerImpl;
import ru.samtakoy.core.business.impl.CoursesRepositoryImpl;
import ru.samtakoy.core.business.impl.NCoursesInteractorImpl;
import ru.samtakoy.core.database.room.MyRoomDb;
import ru.samtakoy.core.database.room.di.DatabaseModule;
import ru.samtakoy.core.di.modules.AppModule;

@Module(includes = {AppModule.class, CardsModule.class, DatabaseModule.class})
public abstract class CoursesModule {


    @Provides
    @Singleton
    static CoursesRepository provideCoursesRepository(MyRoomDb db) {
        return new CoursesRepositoryImpl(db);
    }

    @Binds
    @Singleton
    abstract NCoursesInteractor provideCoursesInteractor(NCoursesInteractorImpl impl);
            /*Context context, QPacksRepository qPacksRep, CoursesRepository corsesRep
    ) {
        return new NCoursesInteractorImpl(context, qPacksRep, corsesRep);
    }*/

    @Binds
    @Singleton
    abstract CoursesPlanner provideCoursesPlanner(CoursesPlannerImpl impl);/*(Context context) {
        return new CoursesPlannerImpl(context);
    }*/


}
