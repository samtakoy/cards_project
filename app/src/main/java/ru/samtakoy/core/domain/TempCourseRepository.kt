package ru.samtakoy.core.domain

import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity

interface TempCourseRepository {

    fun getTempCourseId(): Long

    fun getTempCourse(): LearnCourseEntity

    fun updateTempCourse(course: LearnCourseEntity)

    fun getTempCourseFor(qPackId: Long, cardIds: List<Long>, shuffleCards: Boolean): LearnCourseEntity
}