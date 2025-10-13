package ru.samtakoy.features.import_export.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.app.di.components.AppSingletonScope
import ru.samtakoy.platform.di.PlatformModule
import ru.samtakoy.core.app.di.modules.CardsModule
import ru.samtakoy.core.app.di.modules.CoursesModule
import ru.samtakoy.features.import_export.CoursesExporter
import ru.samtakoy.features.import_export.CoursesExporterImpl
import ru.samtakoy.features.import_export.QPacksExporter
import ru.samtakoy.features.import_export.QPacksExporterImpl
import javax.inject.Singleton

@Module(includes = [PlatformModule::class, CardsModule::class, CoursesModule::class])
interface ExportModule {
    @Binds
    @AppSingletonScope
    fun provideQPacksExporter(impl: QPacksExporterImpl): QPacksExporter

    @Binds
    @AppSingletonScope
    fun provideCoursesExporter(impl: CoursesExporterImpl): CoursesExporter
}
