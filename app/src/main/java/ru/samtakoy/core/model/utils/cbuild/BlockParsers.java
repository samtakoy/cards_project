package ru.samtakoy.core.model.utils.cbuild;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockParsers {

    private static List<String> sMyPrefixes = Arrays.asList(
            CBuilderConst.QPACK_ID_PREFIX,
            CBuilderConst.TITLE_PREFIX,
            CBuilderConst.DESC_PREFIX,
            CBuilderConst.DATE_PREFIX,
            CBuilderConst.VIEWS_PREFIX,
            CBuilderConst.TAGS_PREFIX,
            CBuilderConst.TAGS_PREFIX2
    );
    private static String sStopPrefix = CBuilderConst.QUESTION_PREFIX;


    private boolean mIsFinished;
    private Map<String, StringBuilder> mStringBuilders;
    private StringBuilder mCurStringBuilder;

    public BlockParsers(){

        mIsFinished = false;
        mStringBuilders = new HashMap<>();
    }

    public void processLine(String line, String lowerLine){

        String prefix = readPrefix(lowerLine);
        if(prefix == sStopPrefix){
            mIsFinished = true;
            return;
        }else
        if(prefix.length() > 0){
            int idx = sMyPrefixes.indexOf(prefix);

            if(idx< 0){
                mCurStringBuilder = null;
            } else{
                mCurStringBuilder = new StringBuilder();
                mStringBuilders.put(prefix, mCurStringBuilder);
            }
            line = line.substring(prefix.length()).trim();
        }

        if(mCurStringBuilder != null){
            if(mCurStringBuilder.length() > 0){
                mCurStringBuilder.append(CBuilderConst.LINE_BREAK);
            }
            mCurStringBuilder.append(line);
        }

    }

    private String readPrefix(String line){

        for (String prefix:sMyPrefixes){
            if(line.startsWith(prefix)){
                return prefix;
            }
        }
        if(line.startsWith(sStopPrefix)){
            return sStopPrefix;
        }
        return "";
    }

    public boolean isFinished() {
        return mIsFinished;
    }

    public String get(String prefix, String defaultValue){
        return has(prefix) ? mStringBuilders.get(prefix).toString() : defaultValue;
    }

    public boolean has(String prefix){
        return mStringBuilders.containsKey(prefix);
    }


}
