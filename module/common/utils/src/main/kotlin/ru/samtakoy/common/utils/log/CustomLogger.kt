package ru.samtakoy.common.utils.log

import io.github.aakira.napier.Antilog

abstract class CustomLogger : Antilog() {
    abstract fun getLogs(): List<String>
}