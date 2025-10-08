package ru.samtakoy.features.learncourse.domain.model

import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule
import java.util.Date

data class LearnCourse(
    val id: Long,
    val qPackId: Long,
    val courseType: CourseType,
    val primaryCourseId: Long,
    val title: String,
    val mode: LearnCourseMode,
    val repeatedCount: Int,
    val cardIds: List<Long>,
    val restSchedule: Schedule,
    val realizedSchedule: Schedule,
    val repeatDate: Date
)