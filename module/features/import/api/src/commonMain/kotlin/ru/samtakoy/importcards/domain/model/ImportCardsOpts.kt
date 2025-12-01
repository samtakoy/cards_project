package ru.samtakoy.importcards.domain.model

import java.io.Serializable

enum class ImportCardsOpts(
    /** разрешен импорт новых  */
    val isAllowNewImporting: Boolean,
    /** разрешен апдейт/обработка с id  */
    val isAllowWithIdProcessing: Boolean,
    /** разрешено создание элементов с id, если их не было  */
    val isAllowWithIdCreation: Boolean,
    /** игнорировать все id в файлах  */
    val nullifyId: Boolean
) : Serializable {
    NONE(false, false, false, false),

    /** импорт в чистую базу  */
    TO_BLANK_DB_IMPORT(true, true, true, false),

    /** импортируются только паки без id (новые), паки с id игнорируются  */
    IMPORT_ONLY_NEW(true, false, false, false),

    /** обновление по айдишникам, но запрещено создавать пак с id, пустые игнорируются  */
    UPDATE_EXISTS_ID(false, true, false, false),

    /** вся информация об id в импоритруемом  файле игнорируется  */
    IMPORT_ALL_AS_NEW(true, false, false, true)
}