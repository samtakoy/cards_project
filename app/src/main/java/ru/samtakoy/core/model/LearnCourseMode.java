package ru.samtakoy.core.model;

import java.util.HashMap;
import java.util.Map;

public enum LearnCourseMode {

    TEMPORARY(0),
    PREPARING(1),
    LEARN_WAITING(2),
    LEARNING(3),
    REPEAT_WAITING(4),
    REPEATING(5),
    COMPLETED(6);

    private static final Map<Integer, LearnCourseMode> sIdToEnumMap = new HashMap();
    static {
        for(LearnCourseMode lpm:values()){
            sIdToEnumMap.put(lpm.mId, lpm);
        }
    }

    private final Integer mId;

    LearnCourseMode(Integer id){
        mId = id;
    }

    public Integer getId(){
        return mId;
    }

    public static LearnCourseMode valueOfId(Integer id){
        return  sIdToEnumMap.get(id);
    }

}
