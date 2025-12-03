package ru.samtakoy.oldlegacy.core.oldutils

import java.io.File
import java.io.IOException

object FileUtils {
    @Throws(IOException::class)
    fun deleteDirectoryRecursionJava6(file: File) {
        if (file.isDirectory()) {
            val entries = file.listFiles()
            if (entries != null) {
                for (entry in entries) {
                    deleteDirectoryRecursionJava6(entry)
                }
            }
        }
        if (!file.delete()) {
            throw IOException("Failed to delete " + file)
        }
    }
}
