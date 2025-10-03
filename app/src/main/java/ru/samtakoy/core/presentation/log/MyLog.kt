package ru.samtakoy.core.presentation.log;

import android.util.Log;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyLog {


    private static List<String> sLog = new LinkedList<>();

    public static void add(String source, Throwable t) {
        String s = source + ": " + t.getMessage() + "\n" + ExceptionUtils.getStackTrace(t);
        sLog.add(s);
        Log.e("MyLog", s);
    }

    public static void add(String s) {
        sLog.add(s);
        Log.e("MyLog", s);
    }

    public static List<String> getStrings() {
        return Collections.unmodifiableList(sLog);
    }
}
