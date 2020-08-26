package ru.samtakoy.core.database.room.entities.elements;

import android.content.res.Resources;

import java.util.HashMap;

public class ScheduleItem implements Comparable{

    private static final HashMap<String, ScheduleItem> sMap = new HashMap<>();

    public static ScheduleItem fromString(String srcString){
        ScheduleItem result = sMap.get(srcString);
        if(result == null){
            result = parseString(srcString);
        }
        return result;
    }

    private static ScheduleItem parseString(String srcString) {
        String[] part = srcString.split("(?<=\\d)(?=\\D)");
        int digit = Integer.valueOf(part[0]);
        ScheduleTimeUnit timeUnit = ScheduleTimeUnit.valueOfId(part[1]);
        return new ScheduleItem(digit, timeUnit);
    }

    /*
    public static final List<ScheduleItem> PREDEFINED_ITEMS = new ArrayList<>();
    {
        PREDEFINED_ITEMS.add(new ScheduleItem(1, ScheduleTimeUnit.MINUTE));
        PREDEFINED_ITEMS.add(new ScheduleItem(5, ScheduleTimeUnit.MINUTE));
        PREDEFINED_ITEMS.add(new ScheduleItem(10, ScheduleTimeUnit.MINUTE));
        PREDEFINED_ITEMS.add(new ScheduleItem(30, ScheduleTimeUnit.MINUTE));
        PREDEFINED_ITEMS.add(new ScheduleItem(1, ScheduleTimeUnit.HOUR));
        PREDEFINED_ITEMS.add(new ScheduleItem(2, ScheduleTimeUnit.HOUR));
        PREDEFINED_ITEMS.add(new ScheduleItem(4, ScheduleTimeUnit.HOUR));
        PREDEFINED_ITEMS.add(new ScheduleItem(8, ScheduleTimeUnit.HOUR));
        PREDEFINED_ITEMS.add(new ScheduleItem(1, ScheduleTimeUnit.DAY));
        PREDEFINED_ITEMS.add(new ScheduleItem(2, ScheduleTimeUnit.DAY));
        PREDEFINED_ITEMS.add(new ScheduleItem(3, ScheduleTimeUnit.DAY));
        PREDEFINED_ITEMS.add(new ScheduleItem(1, ScheduleTimeUnit.WEEK));
        PREDEFINED_ITEMS.add(new ScheduleItem(2, ScheduleTimeUnit.WEEK));
        PREDEFINED_ITEMS.add(new ScheduleItem(1, ScheduleTimeUnit.MONTH));
        PREDEFINED_ITEMS.add(new ScheduleItem(2, ScheduleTimeUnit.MONTH));
        PREDEFINED_ITEMS.add(new ScheduleItem(6, ScheduleTimeUnit.MONTH));
        PREDEFINED_ITEMS.add(new ScheduleItem(1, ScheduleTimeUnit.YEAR));
        PREDEFINED_ITEMS.add(new ScheduleItem(2, ScheduleTimeUnit.YEAR));
    }/**/


    private int mDimension;
    private ScheduleTimeUnit mTimeUnit;

    public ScheduleItem(int dimension, ScheduleTimeUnit timeUnit){
        setValue(dimension, timeUnit);
    }

    public ScheduleItem copy(){
        return new ScheduleItem(mDimension, mTimeUnit);
    }

    @Override
    public int compareTo(Object o) {
        ScheduleItem other = (ScheduleItem)o;
        if(other.getTimeUnit() == mTimeUnit){
            return mDimension - other.getDimension();
        }
        return mTimeUnit.ordinal() - other.getTimeUnit().ordinal();
    }

    @Override
    public String toString() {
        return mDimension+mTimeUnit.getId();
    }

    public String toStringView(Resources resources) {
        return mDimension+resources.getString(mTimeUnit.getStringsId());
    }

    public void setValue(int dimension, ScheduleTimeUnit timeUnit) {
        mDimension = dimension;
        mTimeUnit = timeUnit;
    }

    public int getDimension() {
        return mDimension;
    }

    public ScheduleTimeUnit getTimeUnit() {
        return mTimeUnit;
    }

    public int deltaDimension(int value){
        mDimension += value;
        return mDimension;
    }

    public int getMillis() {
        return mDimension * mTimeUnit.getMillis();
    }


}

