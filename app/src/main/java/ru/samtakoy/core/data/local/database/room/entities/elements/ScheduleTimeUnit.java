package ru.samtakoy.core.data.local.database.room.entities.elements;

import java.util.HashMap;

import ru.samtakoy.R;

public enum ScheduleTimeUnit {

    MINUTE(Seconds.MINUTE, "min", R.string.time_min),
    HOUR(Seconds.HOUR, "h", R.string.time_hour),
    DAY(Seconds.DAY, "d", R.string.time_day),
    WEEK(Seconds.WEEK, "w", R.string.time_week),
    MONTH(Seconds.MONTH, "mon", R.string.time_month),
    YEAR(Seconds.YEAR, "y", R.string.time_year);

    private static final HashMap<String, ScheduleTimeUnit> sMapById = new HashMap();
    static {
        for(ScheduleTimeUnit oneItem: ScheduleTimeUnit.values()){
            sMapById.put(oneItem.mId, oneItem);
        }
    }

    private int mSeconds;
    private String mId;
    private int mStringId;

    ScheduleTimeUnit(int seconds, String id, int stringId){
        mSeconds = seconds;
        mId = id;
        mStringId = stringId;
    }

    public static ScheduleTimeUnit valueOfId(String s) {
        return sMapById.get(s);
    }

    public int getSeconds() {
        return mSeconds;
    }

    public String getId() {
        return mId;
    }

    /** TODO используется для визуального отображения - убрать отсюда */
    public int getStringId() {
        return mStringId;
    }

    public int getMillis() {
        return mSeconds*1000;
    }
}
