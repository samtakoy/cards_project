package ru.samtakoy.core.data.local.database.room.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.converters.CourseTypeConverter
import ru.samtakoy.core.data.local.database.room.converters.DateLongConverter
import ru.samtakoy.core.data.local.database.room.converters.LearnCourseModeConverter
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity.Companion._id
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity.Companion._mode
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity.Companion._qpack_id
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity.Companion._repeat_date
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity.Companion.table
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import java.util.*

@Dao
interface LearnCourseDao {

    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getLearnCourseSync(id: Long): LearnCourseEntity?

    @Query("SELECT * FROM $table WHERE $_id=:id")
    suspend fun getLearnCourse(id: Long): LearnCourseEntity?

    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getLearnCourseAsFlow(id: Long): Flow<LearnCourseEntity?>

    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getLearnCourseFlowableRx(id: Long): Flowable<LearnCourseEntity>

    @Query("SELECT * FROM $table WHERE $_mode = :modeId")
    fun getLearnCourseByMode(modeId: Int): LearnCourseEntity?

    @Insert
    fun addLearnCourse(course: LearnCourseEntity): Long

    @Update
    suspend fun updateCourse(course: LearnCourseEntity): Int

    @Query("DELETE FROM $table WHERE $_id=:id")
    suspend fun deleteCourseById(id: Long): Int

    @Query("DELETE FROM $table WHERE $_qpack_id=:qPackId")
    suspend fun deleteQPackCourses(qPackId: Long)

    @Query("SELECT * FROM $table")
    @TypeConverters(CourseTypeConverter::class)
    fun getAllCoursesRx(): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM $table")
    @TypeConverters(CourseTypeConverter::class)
    fun getAllCoursesAsFlow(): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM $table")
    @TypeConverters(CourseTypeConverter::class)
    suspend fun getAllCourses(): List<LearnCourseEntity>

    @Query("SELECT * FROM $table WHERE $_mode IN (:modes) ")
    @TypeConverters(LearnCourseModeConverter::class)
    fun getLearnCourseByModes(modes: List<LearnCourseMode>): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM $table WHERE $_mode IN (:modes) ")
    @TypeConverters(LearnCourseModeConverter::class)
    fun getLearnCourseByModesNow(modes: List<LearnCourseMode>): List<LearnCourseEntity>

    @Query("SELECT * FROM $table WHERE $_id IN (:coursesIds) ")
    fun getCoursesByIds(coursesIds: List<Long>): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM $table WHERE $_id IN (:coursesIds) ")
    fun getCoursesByIdsAsFlow(coursesIds: List<Long>): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM $table WHERE $_mode IN (:modes) ")
    fun getCoursesByModes(modes: List<Int>): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM $table WHERE $_mode IN (:modes) ")
    fun getCoursesByModesAsFlow(modes: List<Int>): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM $table WHERE $_qpack_id = :qPackId ")
    fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourseEntity>>

    @Query("SELECT * FROM $table WHERE $_qpack_id = :qPackId ")
    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourseEntity>>

    @Query("SELECT * FROM $table WHERE $_mode = :mode AND $_repeat_date <= :repeatDate ORDER BY $_repeat_date ASC")
    @TypeConverters(LearnCourseModeConverter::class, DateLongConverter::class)
    fun getOrderedCoursesLessThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourseEntity>

    @Query("SELECT * FROM $table WHERE $_mode = :mode AND $_repeat_date > :repeatDate ORDER BY $_repeat_date ASC")
    @TypeConverters(LearnCourseModeConverter::class, DateLongConverter::class)
    fun getOrderedCoursesMoreThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourseEntity>

}