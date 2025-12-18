package ru.samtakoy.core.log

import io.github.aakira.napier.Antilog

abstract class CustomLogger : Antilog() {
    abstract fun getLogs(): List<String>
}