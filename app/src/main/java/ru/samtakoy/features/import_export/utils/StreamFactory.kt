package ru.samtakoy.features.import_export.utils

import java.io.InputStream

interface StreamFactory {
    @Throws(Exception::class)
    fun openStream(): InputStream
    val srcPath: String
    val themeId: Long
    val fileName: String
}
