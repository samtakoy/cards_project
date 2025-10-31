package ru.samtakoy.domain.exportcards

interface CoursesExporter {
    suspend fun exportAllToEmail()
}
