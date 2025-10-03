package ru.samtakoy.features.import_export

interface CoursesExporter {
    suspend fun exportAllToEmail()
}
