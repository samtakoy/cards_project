package ru.samtakoy.core.app.utils;

import java.util.Date;

public class DateUtils {

    public static long getTimeLong(Date date) {
        return date.getTime();
    }

    public static long getCurrentTimeLong() {
        return System.currentTimeMillis();
    }

    public static long getMillisTo(long targetUtc) {
        return targetUtc - System.currentTimeMillis();
    }

    public static long getMillisTo(Date date) {
        return getMillisTo(dateToDbSerialized(date));
    }

    public static Date getCurrentTimeDate() {
        return getDateFromLong(getCurrentTimeLong());
    }

    public static long getDateAfterCurrentLong(int millisDelta) {
        return getCurrentTimeLong() + millisDelta;
    }

    public static long dateToDbSerialized(Date d){
        return getTimeLong(d);
    }

    public static Date dateFromDbSerialized(long utcTimeMillis){
        return getDateFromLong(utcTimeMillis);
    }


    public static Date getDateFromLong(long value) {
        return new Date(value);
    }

}
