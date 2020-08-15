package ru.samtakoy.core.services.import_utils;

public class ImportCardsException extends Exception{


    public static final int ERR_PACK_ID_MISSING = 1;
    public static final int ERR_PACK_WITH_ID_CREATION_NOT_ALLOWED = 2;
    public static final int ERR_WRONG_CARD_PACK = 3;

    private int mErrorId;

    public ImportCardsException(int errorId, String message){
        super(message);

        mErrorId = errorId;
    }

    public int getErrorId(){
        return mErrorId;
    }

}
