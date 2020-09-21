package ru.samtakoy.core.data.local.database.room.entities.types;

import java.util.HashMap;
import java.util.Map;


public enum CourseType {


    // обычный курс
    PRIMARY(1),
    // курс, для новодобавленных карточек, догоняет обычный
    SECONDARY(2),
    // дополнительное повторение, заланированное по желанию пользователя
    ADDITIONAL(3),

    // временный курс для служебных целей, не видимый пользователю
    TEMPORARY(4);


    private static final Map<Integer, CourseType> sIdToEnumMap = new HashMap();

    static {
        for (CourseType courseType : values()) {
            sIdToEnumMap.put(courseType.mDatabaseId, courseType);
        }
    }

    private final Integer mDatabaseId;

    CourseType(Integer dbId) {
        mDatabaseId = dbId;
    }

    public Integer getDbId() {
        return mDatabaseId;
    }

    public static CourseType valueOfId(Integer id) {
        return sIdToEnumMap.get(id);
    }
}
