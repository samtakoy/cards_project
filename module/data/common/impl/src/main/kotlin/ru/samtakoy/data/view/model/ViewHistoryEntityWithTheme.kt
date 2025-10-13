package ru.samtakoy.data.view.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

internal class ViewHistoryEntityWithTheme(
    @Embedded
    val historyItem: ViewHistoryEntity,

    @ColumnInfo(name = QPACK_TITLE)
    val qPackTitle: String,

    @ColumnInfo(name = THEME_TITLE)
    val themeTitle: String?
) {
    companion object {
        const val QPACK_TITLE = "qpack_title"
        const val THEME_TITLE = "theme_title"
    }
}