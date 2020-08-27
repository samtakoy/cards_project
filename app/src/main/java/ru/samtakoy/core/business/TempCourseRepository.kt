package ru.samtakoy.core.business

import ru.samtakoy.core.database.room.entities.LearnCourseEntity

interface TempCourseRepository {

    fun getTempCourseId(): Long

    fun getTempCourse(): LearnCourseEntity

    fun updateTempCourse(course: LearnCourseEntity)

    fun getTempCourseFor(qPackId: Long, cardIds: List<Long>, shuffleCards: Boolean): LearnCourseEntity
}