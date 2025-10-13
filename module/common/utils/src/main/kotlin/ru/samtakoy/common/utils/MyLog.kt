package ru.samtakoy.common.utils

import android.util.Log
import java.util.Collections
import java.util.LinkedList

object MyLog {
    private val sLog: MutableList<String> = LinkedList<String>()

    @JvmStatic
    fun add(source: String, t: Throwable?) {
        val s = source + ": " + t?.message + "\n" + t?.stackTraceToString()
        sLog.add(s)
        Log.e("MyLog", s)
    }

    @JvmStatic
    fun add(s: String) {
        sLog.add(s)
        Log.e("MyLog", s)
    }

    @JvmStatic
    val strings: MutableList<String>
        get() = Collections.unmodifiableList<String>(sLog)
}