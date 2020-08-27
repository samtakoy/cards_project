package ru.samtakoy.core.database.room.entities.types;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum LearnCourseMode {


    PREPARING(1),
    LEARN_WAITING(2),
    LEARNING(3),
    REPEAT_WAITING(4),
    REPEATING(5),
    COMPLETED(6);

    private static final Map<Integer, LearnCourseMode> sIdToEnumMap = new HashMap();

    static {
        for (LearnCourseMode lpm : values()) {
            sIdToEnumMap.put(lpm.mDatabaseId, lpm);
        }
    }

    private final Integer mDatabaseId;

    LearnCourseMode(Integer dbId) {
        mDatabaseId = dbId;
    }

    public Integer getDbId() {
        return mDatabaseId;
    }

    public static LearnCourseMode valueOfId(Integer dbId) {
        return sIdToEnumMap.get(dbId);
    }

    @Nullable
    public static int[] listToPrimitiveArray(@Nullable List<LearnCourseMode> modesList) {

        if (modesList == null) {
            return null;
        }
        int[] result = new int[modesList.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = modesList.get(i).getDbId();
        }
        return result;
    }

    @Nullable
    public static List<LearnCourseMode> primitiveArrayToList(@Nullable int[] modeIdArray) {

        if (modeIdArray == null) {
            return null;
        }
        List<LearnCourseMode> result = new ArrayList<>(modeIdArray.length);
        for (int i = 0; i < modeIdArray.length; i++) {
            result.add(valueOfId(modeIdArray[i]));
        }
        return result;
    }

}
