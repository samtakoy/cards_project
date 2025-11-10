package ru.samtakoy.domain.learncourse

import ru.samtakoy.domain.learncourse.schedule.Schedule
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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
    @OptIn(ExperimentalTime::class)
    val repeatDate: Instant
)