package ru.samtakoy.features.import_export.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.app.di.components.AppSingletonScope
import ru.samtakoy.features.import_export.CoursesExporter
import ru.samtakoy.features.import_export.CoursesExporterImpl
import ru.samtakoy.features.import_export.QPacksExporter
import ru.samtakoy.features.import_export.QPacksExporterImpl

@Module
interface ExportModule {
    @Binds
    @AppSingletonScope
    fun provideQPacksExporter(impl: QPacksExporterImpl): QPacksExporter

    @Binds
    @AppSingletonScope
    fun provideCoursesExporter(impl: CoursesExporterImpl): CoursesExporter
}
