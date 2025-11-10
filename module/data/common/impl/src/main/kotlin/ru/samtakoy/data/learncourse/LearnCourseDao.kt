package ru.samtakoy.data.learncourse

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.data.learncourse.converters.CourseTypeConverter
import ru.samtakoy.data.learncourse.converters.LearnCourseModeConverter
import ru.samtakoy.data.learncourse.model.LearnCourseEntity
import ru.samtakoy.data.learncourse.model.LearnCourseModeEntity

@Dao
internal interface LearnCourseDao {

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id}=:id")
    fun getLearnCourseSync(id: Long): LearnCourseEntity?

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id}=:id")
    suspend fun getLearnCourse(id: Long): LearnCourseEntity?

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id}=:id")
    fun getLearnCourseAsFlow(id: Long): Flow<LearnCourseEntity?>

    @Insert
    fun addLearnCourse(course: LearnCourseEntity): Long

    @Update
    suspend fun updateCourse(course: LearnCourseEntity): Int

    @Query("DELETE FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id}=:id")
    suspend fun deleteCourseById(id: Long): Int

    @Query("DELETE FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._qpack_id}=:qPackId")
    suspend fun deleteQPackCourses(qPackId: Long)

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table}")
    @TypeConverters(CourseTypeConverter::class)
    fun getAllCoursesAsFlow(): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table}")
    @TypeConverters(CourseTypeConverter::class)
    suspend fun getAllCourses(): List<LearnCourseEntity>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} IN (:modes) ")
    @TypeConverters(LearnCourseModeConverter::class)
    fun getLearnCourseByModesNow(modes: List<LearnCourseModeEntity>): List<LearnCourseEntity>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id} IN (:coursesIds) ")
    fun getCoursesByIdsAsFlow(coursesIds: List<Long>): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} IN (:modes) ")
    fun getCoursesByModesAsFlow(modes: List<Int>): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._qpack_id} = :qPackId ")
    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} = :mode AND ${LearnCourseEntity.Companion._repeat_date} <= :repeatDate ORDER BY ${LearnCourseEntity.Companion._repeat_date} ASC")
    @TypeConverters(LearnCourseModeConverter::class)
    fun getOrderedCoursesLessThan(mode: LearnCourseModeEntity, repeatDate: Long): List<LearnCourseEntity>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} = :mode AND ${LearnCourseEntity.Companion._repeat_date} > :repeatDate ORDER BY ${LearnCourseEntity.Companion._repeat_date} ASC")
    @TypeConverters(LearnCourseModeConverter::class)
    fun getOrderedCoursesMoreThan(mode: LearnCourseModeEntity, repeatDate: Long): List<LearnCourseEntity>

}