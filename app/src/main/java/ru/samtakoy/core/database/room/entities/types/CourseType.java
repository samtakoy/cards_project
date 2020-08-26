package ru.samtakoy.core.database.room.entities.types;

import java.util.HashMap;
import java.util.Map;

public enum CourseType {


    // обычный курс
    PRIMARY(1),
    // курс, для новодобавленных карточек, догоняет обычный
    SECONDARY(2),
    // дополнительное повторение, заланированное по желанию пользователя
    ADDITIONAL(3);

    private static final Map<Integer, CourseType> sIdToEnumMap = new HashMap();
    static {
        for(CourseType courseType:values()){
            sIdToEnumMap.put(courseType.mId, courseType);
        }
    }

    private final Integer mId;

    CourseType(Integer id){
        mId = id;
    }

    public Integer getId(){
        return mId;
    }

    public static CourseType valueOfId(Integer id){
        return  sIdToEnumMap.get(id);
    }
}
