package ru.samtakoy.core.model.elements;

import java.util.HashMap;

import ru.samtakoy.R;

public enum ScheduleTimeUnit {

    MINUTE(Seconds.MINUTE, "min", R.string.time_min, 0x990000),
    HOUR(Seconds.HOUR, "h", R.string.time_hour, 0x009900),
    DAY(Seconds.DAY, "d", R.string.time_day, 0x0000999),
    WEEK(Seconds.WEEK, "w", R.string.time_week, 0x006666),
    MONTH(Seconds.MONTH, "mon", R.string.time_month, 0x666600),
    YEAR(Seconds.YEAR, "y", R.string.time_year, 0x660066);

    private static final HashMap<String, ScheduleTimeUnit> sMapById = new HashMap();
    static {
        for(ScheduleTimeUnit oneItem: ScheduleTimeUnit.values()){
            sMapById.put(oneItem.mId, oneItem);
        }
    }

    private int mSeconds;
    private String mId;
    private int mStringsId;
    private int mColor;

    ScheduleTimeUnit(int seconds, String id, int stringsId, int color){
        mSeconds = seconds;
        mId = id;
        mStringsId = stringsId;
        mColor = color;
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

    public int getStringsId() {
        return mStringsId;
    }

    public int getColor() {
        return mColor;
    }

    public int getMillis() {
        return mSeconds*1000;
    }
}
