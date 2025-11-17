package ru.samtakoy.presentation.themes.list.model

import ru.samtakoy.presentation.core.design_system.base.model.LongUiId

internal object ThemeListMenuId {
    private var idGenerator: Long = 0

    val RootMenu: LongUiId = LongUiId(++idGenerator)

    val ImportCards: LongUiId = LongUiId(++idGenerator)
    val ImportFromFolderAll: LongUiId = LongUiId(++idGenerator)

    val FromFolderSubMenu: LongUiId = LongUiId(++idGenerator)
        val FromFolderImportNew: LongUiId = LongUiId(++idGenerator)
        val FromFolderUpdateExists: LongUiId = LongUiId(++idGenerator)
        val FromFolderImportAsNew: LongUiId = LongUiId(++idGenerator)

    /** Импорт из zip в чистую базу */
    val ImportFromZipAll: LongUiId = LongUiId(++idGenerator)
    val FromZipSubMenu: LongUiId = LongUiId(++idGenerator)
        /** Импорт из zip в существующую базу дополнительных наборов */
        val FromZipImportNew: LongUiId = LongUiId(++idGenerator)
        val FromZipUpdateExists: LongUiId = LongUiId(++idGenerator)
        val FromZipImportAsNew: LongUiId = LongUiId(++idGenerator)

    val OnlineImportCards: LongUiId = LongUiId(++idGenerator)
    val ExportAllToDir: LongUiId = LongUiId(++idGenerator)
    val ExportAllToEmail: LongUiId = LongUiId(++idGenerator)

    val Log: LongUiId = LongUiId(++idGenerator)

    val DownSeparator: LongUiId = LongUiId(++idGenerator)

    val Settings: LongUiId = LongUiId(++idGenerator)
} 


