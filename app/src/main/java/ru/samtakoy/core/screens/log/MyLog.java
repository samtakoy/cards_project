package ru.samtakoy.core.screens.log;

import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyLog {


    private static List<String> sLog = new LinkedList<>();

    public static void add(String s){
        sLog.add(s);
        Log.e("MyLog", s);
    }

    public static List<String> getStrings(){
        return Collections.unmodifiableList(sLog);
    }
}
