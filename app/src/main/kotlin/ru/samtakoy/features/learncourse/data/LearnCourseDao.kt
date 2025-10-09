package ru.samtakoy.features.learncourse.data

import androidx.room.*
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.features.database.data.converters.DateLongConverter
import ru.samtakoy.features.learncourse.data.converters.CourseTypeConverter
import ru.samtakoy.features.learncourse.data.converters.LearnCourseModeConverter
import ru.samtakoy.features.learncourse.data.model.LearnCourseEntity
import ru.samtakoy.features.learncourse.data.model.LearnCourseModeEntity
import java.util.*

@Dao
interface LearnCourseDao {

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id}=:id")
    fun getLearnCourseSync(id: Long): LearnCourseEntity?

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id}=:id")
    suspend fun getLearnCourse(id: Long): LearnCourseEntity?

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id}=:id")
    fun getLearnCourseAsFlow(id: Long): Flow<LearnCourseEntity?>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id}=:id")
    fun getLearnCourseFlowableRx(id: Long): Flowable<LearnCourseEntity>

    // @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} = :modeId")
    // fun getLearnCourseByMode(modeId: Int): LearnCourseEntity?

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
    fun getAllCoursesRx(): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table}")
    @TypeConverters(CourseTypeConverter::class)
    fun getAllCoursesAsFlow(): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table}")
    @TypeConverters(CourseTypeConverter::class)
    suspend fun getAllCourses(): List<LearnCourseEntity>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} IN (:modes) ")
    @TypeConverters(LearnCourseModeConverter::class)
    fun getLearnCourseByModes(modes: List<LearnCourseModeEntity>): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} IN (:modes) ")
    @TypeConverters(LearnCourseModeConverter::class)
    fun getLearnCourseByModesNow(modes: List<LearnCourseModeEntity>): List<LearnCourseEntity>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id} IN (:coursesIds) ")
    fun getCoursesByIds(coursesIds: List<Long>): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._id} IN (:coursesIds) ")
    fun getCoursesByIdsAsFlow(coursesIds: List<Long>): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} IN (:modes) ")
    fun getCoursesByModes(modes: List<Int>): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} IN (:modes) ")
    fun getCoursesByModesAsFlow(modes: List<Int>): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._qpack_id} = :qPackId ")
    fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._qpack_id} = :qPackId ")
    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} = :mode AND ${LearnCourseEntity.Companion._repeat_date} <= :repeatDate ORDER BY ${LearnCourseEntity.Companion._repeat_date} ASC")
    @TypeConverters(LearnCourseModeConverter::class, DateLongConverter::class)
    fun getOrderedCoursesLessThan(mode: LearnCourseModeEntity, repeatDate: Date): List<LearnCourseEntity>

    @Query("SELECT * FROM ${LearnCourseEntity.Companion.table} WHERE ${LearnCourseEntity.Companion._mode} = :mode AND ${LearnCourseEntity.Companion._repeat_date} > :repeatDate ORDER BY ${LearnCourseEntity.Companion._repeat_date} ASC")
    @TypeConverters(LearnCourseModeConverter::class, DateLongConverter::class)
    fun getOrderedCoursesMoreThan(mode: LearnCourseModeEntity, repeatDate: Date): List<LearnCourseEntity>

}