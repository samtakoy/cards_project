package ru.samtakoy.core.model.utils;

import android.content.res.Resources;

public class TimeViewUtils {

    public static String getTimeView(Resources res, int millis){
        // TODO;
        return String.valueOf((millis/1000));
    }

}
