package ru.samtakoy.common.utils.log

import io.github.aakira.napier.Napier

@Deprecated("Use [Napier] with MyLog.tag")
object MyLog {
    const val tag = "MyLog"

    fun add(source: String, t: Throwable?) {
        Napier.e(tag = tag, throwable = t) { source }
    }

    fun add(s: String) {
        Napier.e(tag = tag) { s }
    }
}