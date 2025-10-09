package ru.samtakoy.features.learncourseview.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LearnCourseViewDao {

    @Insert
    suspend fun add(item: LearnCourseViewEntity)

    @Query(
        """SELECT ${LearnCourseViewEntity.VIEW_ID}
            FROM ${LearnCourseViewEntity.TABLE_NAME}
            WHERE ${LearnCourseViewEntity.COURSE_ID}=:courseId
            ORDER BY ${LearnCourseViewEntity.VIEW_ID} DESC
            LIMIT 1"""
    )
    suspend fun getCourseLastViewId(courseId: Long): Long?

    @Query(
        """SELECT ${LearnCourseViewEntity.VIEW_ID}
            FROM ${LearnCourseViewEntity.TABLE_NAME}
            WHERE ${LearnCourseViewEntity.COURSE_ID}=:courseId
            ORDER BY ${LearnCourseViewEntity.VIEW_ID} DESC
            LIMIT 1"""
    )
    fun getCourseLastViewIdAsFlow(courseId: Long): Flow<Long>

    @Query(
        """SELECT ${LearnCourseViewEntity.COURSE_ID}
            FROM ${LearnCourseViewEntity.TABLE_NAME}
            WHERE ${LearnCourseViewEntity.VIEW_ID}=:viewId"""
    )
    suspend fun getCourseIdForViewId(viewId: Long): Long?
}