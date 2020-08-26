package ru.samtakoy.features.import_export.utils;

import java.io.Serializable;

public enum ImportCardsOpts implements Serializable {

    NONE(false, false, false, false),
    /** импорт в чистую базу */
    TO_BLANK_DB_IMPORT(true, true, true, false),
    /** импортируются только паки без id (новые), паки с id игнорируются */
    IMPORT_ONLY_NEW(true, false, false, false),
    /** обновление по айдишникам, но запрещено создавать пак с id, пустые игнорируются */
    UPDATE_EXISTS_ID(false, true, false, false),
    /** вся информация об id в импоритруемом  файле игнорируется */
    IMPORT_ALL_AS_NEW(true, false, false, true);


    /** разрешен импорт новых */
    private boolean mAllowNewImporting;
    /** разрешен апдейт/обработка с id */
    private boolean mAllowWithIdProcessing;
    /** разрешено создание элементов с id, если их не было */
    private boolean mAllowWithIdCreation;
    /** игнорировать все id в файлах */
    private boolean mNullifyId;


    ImportCardsOpts(boolean allowNewImporting, boolean allowWithIdProcessing, boolean allowWithIdCreation, boolean nullifyId) {
        mAllowNewImporting = allowNewImporting;
        mAllowWithIdProcessing = allowWithIdProcessing;
        mAllowWithIdCreation = allowWithIdCreation;
        mNullifyId = nullifyId;

    }

    public boolean getNullifyId() {
        return mNullifyId;
    }

    public boolean isAllowNewImporting() {
        return mAllowNewImporting;
    }

    public boolean isAllowWithIdProcessing() {
        return mAllowWithIdProcessing;
    }

    public boolean isAllowWithIdCreation() {
        return mAllowWithIdCreation;
    }
}
