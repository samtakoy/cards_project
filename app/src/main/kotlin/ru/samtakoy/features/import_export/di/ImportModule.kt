package ru.samtakoy.features.import_export.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.app.di.components.AppSingletonScope
import ru.samtakoy.features.import_export.ImportApi
import ru.samtakoy.features.import_export.ImportApiImpl
import javax.inject.Singleton

@Module
abstract class ImportModule {


    @Binds
    @AppSingletonScope
    abstract fun provideImportApi(impl: ImportApiImpl): ImportApi;
}