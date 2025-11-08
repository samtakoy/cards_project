package ru.samtakoy.features.import_export.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.core.presentation.export_cards.mv.BatchExportViewModelImpl
import ru.samtakoy.domain.exportcards.CoursesExporter
import ru.samtakoy.domain.exportcards.QPacksExporter
import ru.samtakoy.features.import_export.CoursesExporterImpl
import ru.samtakoy.features.import_export.QPacksExporterImpl

internal fun exportModule() = module {
    // domain
    factoryOf(::QPacksExporterImpl) bind QPacksExporter::class
    factoryOf(::CoursesExporterImpl) bind CoursesExporter::class

    // presentation
    viewModelOf(::BatchExportViewModelImpl)
}
