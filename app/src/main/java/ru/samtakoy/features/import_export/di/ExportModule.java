package ru.samtakoy.features.import_export.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.samtakoy.core.app.di.modules.AppModule;
import ru.samtakoy.core.app.di.modules.CardsModule;
import ru.samtakoy.core.app.di.modules.CoursesModule;
import ru.samtakoy.core.domain.CoursesRepository;
import ru.samtakoy.features.import_export.CoursesExporter;
import ru.samtakoy.features.import_export.CoursesExporterImpl;
import ru.samtakoy.features.import_export.QPacksExporter;
import ru.samtakoy.features.import_export.QPacksExporterImpl;

@Module(includes = {AppModule.class, CardsModule.class, CoursesModule.class})
public abstract class ExportModule {


    @Binds
    @Singleton
    public abstract QPacksExporter provideQPacksExporter(QPacksExporterImpl impl);
        /*(Context ctx, CardsRepository cardsRep)
    {
        return new QPacksExporterImpl(ctx, cardsRep);
    }*/

    @Provides
    @Singleton
    public static CoursesExporter provideCoursesExporter(Context ctx, CoursesRepository coursesRep) {
        return new CoursesExporterImpl(ctx, coursesRep);
    }

}
