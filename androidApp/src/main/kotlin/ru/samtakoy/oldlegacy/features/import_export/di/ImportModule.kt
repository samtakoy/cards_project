package ru.samtakoy.oldlegacy.features.import_export.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.oldlegacy.features.import_export.ImportApi
import ru.samtakoy.oldlegacy.features.import_export.ImportApiImpl

internal fun importModule() = module {
    factoryOf(::ImportApiImpl) bind ImportApi::class
}