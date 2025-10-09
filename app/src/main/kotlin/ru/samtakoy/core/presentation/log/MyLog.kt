package ru.samtakoy.core.presentation.log

import android.util.Log
import org.apache.commons.lang3.exception.ExceptionUtils
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
