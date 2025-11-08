package ru.samtakoy.features.import_export.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.features.import_export.ImportApi
import ru.samtakoy.features.import_export.ImportApiImpl

internal fun importModule() = module {
    factoryOf(::ImportApiImpl) bind ImportApi::class
}