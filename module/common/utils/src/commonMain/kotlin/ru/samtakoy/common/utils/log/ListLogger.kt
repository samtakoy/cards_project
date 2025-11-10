package ru.samtakoy.common.utils.log

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel

internal class ListLogger(
    private val targetTag: String,
    private val logs: MutableList<String> = mutableListOf()
) : Antilog() {

    fun getLogs(): List<String> = logs

    override fun isEnable(priority: LogLevel, tag: String?): Boolean {
        return tag == targetTag
    }

    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        if (tag == targetTag && message != null) {
            logs.add(
                if (throwable != null) {
                    "[$tag] ${priority.name}: $message, ${throwable.stackTraceToString()}"
                } else {
                    "[$tag] ${priority.name}: $message"
                }
            )
        }
    }
}