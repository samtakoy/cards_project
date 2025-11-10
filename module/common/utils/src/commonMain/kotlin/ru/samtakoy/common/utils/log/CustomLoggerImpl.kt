package ru.samtakoy.common.utils.log

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel

/**
 * Using: Napier.base(customLogger)
 * */
internal class CustomLoggerImpl(
    targetTag: String,
    logs: MutableList<String> = mutableListOf()
) : CustomLogger() {

    private val consoleLogger = DebugAntilog()
    private val listLogger = ListLogger(targetTag, logs)

    override fun getLogs(): List<String> = listLogger.getLogs()

    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        consoleLogger.log(priority, tag, throwable, message)
        listLogger.log(priority, tag, throwable, message)
    }
}