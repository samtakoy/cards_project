package ru.samtakoy.oldlegacy.core.presentation.export_cards

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface BatchExportType : Parcelable {
    @Parcelize
    object Courses : BatchExportType
    @Parcelize
    object QPacksToEmail : BatchExportType
    @Parcelize
    class QPacksToDir(val exportDirPath: String) : BatchExportType
}