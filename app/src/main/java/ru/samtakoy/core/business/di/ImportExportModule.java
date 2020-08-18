package ru.samtakoy.core.business.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.CoursesExporter;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.QPacksExporter;
import ru.samtakoy.core.business.impl.CoursesExporterImpl;
import ru.samtakoy.core.business.impl.QPacksExporterImpl;
import ru.samtakoy.core.di.modules.AppModule;

@Module(includes = {AppModule.class, CardsModule.class, CoursesModule.class})
public abstract class ImportExportModule {


    @Provides
    @Singleton
    public static QPacksExporter provideQPacksExporter(Context ctx, CardsRepository cardsRep) {
        return new QPacksExporterImpl(ctx, cardsRep);
    }

    @Provides
    @Singleton
    public static CoursesExporter provideCoursesExporter(Context ctx, CoursesRepository coursesRep) {
        return new CoursesExporterImpl(ctx, coursesRep);
    }

}
