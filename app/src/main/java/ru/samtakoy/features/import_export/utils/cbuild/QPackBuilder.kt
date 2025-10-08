package ru.samtakoy.features.import_export.utils.cbuild;

import java.util.HashMap;
import java.util.List;

import ru.samtakoy.core.data.local.database.room.entities.QPackEntityKt;
import ru.samtakoy.core.data.local.database.room.entities.TagEntity;
import ru.samtakoy.features.import_export.utils.ImportCardsException;

public class QPackBuilder {


    private BlockParsers mBlockParsers;
    private CardsParser mCardsParser;

    private Long mThemeId;
    private Long mQPackId;
    private String mSrcFilePath;
    private String mFileName;


    private int mBuildersCount = 1;
    private int mBuilderNum = 1;

    private boolean mNullifyId;

    public QPackBuilder(
            Long themeId,
            String srcPath,
            HashMap<String, TagEntity> tagMap,
            String fileName,
            boolean nullifyId
    ){

        mThemeId = themeId;
        mQPackId = CBuilderConst.NO_ID;
        mSrcFilePath = srcPath;
        mFileName = fileName;
        mNullifyId = nullifyId;

        //mTargetQPack = null;


        mBlockParsers = new BlockParsers();
        mCardsParser = new CardsParser(tagMap, nullifyId);
    }

    public Long getThemeId() {
        return mThemeId;
    }

    public String getSrcFilePath() {
        return mSrcFilePath;
    }


    public void addLine(String line){

        String lowerLine = line.toLowerCase();

        if(!mBlockParsers.isFinished()){
            mBlockParsers.processLine(line, lowerLine);
            if(!mBlockParsers.isFinished()){
                return;
            }else{
                mQPackId = mNullifyId ? CBuilderConst.NO_ID : getParsedId();
                mCardsParser.onStart(mQPackId);
            }
        }

        mCardsParser.processLine(line, lowerLine);
    }

    public QPackBuilder build() throws ImportCardsException {
        mCardsParser.onFinalize(mQPackId);

        if(!hasIncomingId() && hasAnyCardId()){
            throw new ImportCardsException(ImportCardsException.ERR_PACK_ID_MISSING, "");
        }

        return this;
    }

    public boolean hasIncomingId(){
        return mBlockParsers.has(CBuilderConst.QPACK_ID_PREFIX);
    }


    public Long getParsedId(){
        return hasIncomingId() ? Long.parseLong(mBlockParsers.get(CBuilderConst.QPACK_ID_PREFIX, CBuilderConst.NO_ID_STR)) : CBuilderConst.NO_ID;
    }/**/

    public String getTitle() {
        return mBlockParsers.get(CBuilderConst.TITLE_PREFIX, mFileName);
    }

    public String getFileName(){
        return mFileName;
    }

    public String getDesc() {
        return mBlockParsers.get(CBuilderConst.DESC_PREFIX, "");
    }

    public boolean hasCreationDate(){
        return mBlockParsers.has(CBuilderConst.DATE_PREFIX);
    }

    public String getCreationDate() {
        return mBlockParsers.get(CBuilderConst.DATE_PREFIX, QPackEntityKt.DEF_DATE).trim();
    }

    public boolean hasViewCount(){
        return mBlockParsers.has(CBuilderConst.VIEWS_PREFIX);
    }

    public int getViewCount() {
        String stringResult = mBlockParsers.get(CBuilderConst.VIEWS_PREFIX, "0").trim();
         return Integer.parseInt(stringResult);
    }

    public List<CardBuilder> getCardBuilders() {
        return mCardsParser.getCardBuilders();
    }

    public void setTargetQPack(Long qPackId) {
        mQPackId = qPackId;
    }

    public int getBuildersCount() {
        return mBuildersCount;
    }

    public void setBuildersCount(int buildersCount) {
        mBuildersCount = buildersCount;
    }

    public int getBuilderNum() {
        return mBuilderNum;
    }

    public void setBuilderNum(int builderNum) {
        mBuilderNum = builderNum;
    }


    public boolean hasAnyCardId() {
        for(CardBuilder cBuilder:getCardBuilders()){
            if(cBuilder.hasId()){
                return true;
            }
        }
        return false;
    }

}
