package ru.samtakoy.core.presentation.export_cards.mv

import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.presentation.export_cards.BatchExportType
import ru.samtakoy.core.presentation.export_cards.mv.BatchExportViewModel.Action
import ru.samtakoy.common.utils.log.MyLog
import ru.samtakoy.domain.exportcards.CoursesExporter
import ru.samtakoy.domain.exportcards.QPacksExporter
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl

internal class BatchExportViewModelImpl(
    coursesExporter: CoursesExporter,
    qPacksExporter: QPacksExporter,
    private val resources: Resources,
    scopeProvider: ScopeProvider,
    type: BatchExportType
) : BaseViewModelImpl<Unit, Action, Unit>(
    scopeProvider = scopeProvider,
    initialState = Unit
), BatchExportViewModel {

    init {
        launchCatching(
            onError = ::onGetError
        ) {
            when (type) {
                BatchExportType.Courses -> {
                    exportCoursesToEmail(coursesExporter)
                }
                is BatchExportType.QPacksToDir -> {
                    exportQPacksToDir(qPacksExporter, type.exportDirPath)
                }
                BatchExportType.QPacksToEmail -> {
                    exportQPacksToEmail(qPacksExporter)
                }
            }
        }
    }

    override fun onEvent(event: Unit) = Unit

    private fun onGetError(t: Throwable) {
        MyLog.add("batch export error: " + ExceptionUtils.getStackTrace(t))
        sendAction(
            Action.ExitWithError(resources.getString(R.string.fragment_dialog_batch_export_error_msg))
        )
    }

    private suspend fun exportCoursesToEmail(exporter: CoursesExporter) {
        exporter.exportAllToEmail()
        sendAction(Action.ExitOk)
    }

    private suspend fun exportQPacksToEmail(qPacksExporter: QPacksExporter) {
        qPacksExporter.exportAllToEmail()
        sendAction(Action.ExitOk)
    }

    private suspend fun exportQPacksToDir(qPacksExporter: QPacksExporter, dir: String) {
        qPacksExporter.exportAllToFolder(dir)
        sendAction(Action.ExitOk)
    }
}