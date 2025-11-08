package ru.samtakoy.features.import_export.utils

fun isPackFile(fileName: String): Boolean {
    return getExtension(fileName) == "txt"
}

fun getExtension(fileName: String): String {
    val idx = fileName.lastIndexOf(".")
    return if (idx > 0) {
        fileName.substring(idx + 1)
    } else ""
}
