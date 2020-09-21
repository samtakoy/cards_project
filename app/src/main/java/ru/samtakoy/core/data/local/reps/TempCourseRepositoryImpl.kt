package ru.samtakoy.core.data.local.reps

import com.google.gson.Gson
import ru.samtakoy.core.app.utils.DateUtils
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity.Companion.createNewPreparing
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule
import ru.samtakoy.core.data.local.database.room.entities.types.CourseType
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.data.local.preferences.AppPreferences
import ru.samtakoy.core.data.local.preferences.TEMP_COURSE
import ru.samtakoy.core.domain.TempCourseRepository
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject
import kotlin.concurrent.withLock

class TempCourseRepositoryImpl @Inject constructor(
        val preferences: AppPreferences,
        val gson: Gson
) : TempCourseRepository {

    private val lock: Lock = ReentrantLock();

    override fun getTempCourseId(): Long {
        return Long.MAX_VALUE;
    }

    override fun getTempCourse(): LearnCourseEntity {
        lock.withLock {
            val json: String? = preferences.getString(TEMP_COURSE, null)
            lateinit var result: LearnCourseEntity;
            if (json == null) {
                result = createNewPreparing(
                        0, "", LearnCourseMode.LEARNING, emptyList(), Schedule.createEmpty(), java.util.Date()
                )
                result.id = getTempCourseId()
                result.courseType = CourseType.TEMPORARY
            } else {
                result = gson.fromJson(json, LearnCourseEntity::class.java)
            }
            return result
        }
    }

    override fun updateTempCourse(course: LearnCourseEntity) {
        lock.withLock {
            preferences.setString(TEMP_COURSE, gson.toJson(course))
        }
    }


    // ---
    override fun getTempCourseFor(qPackId: Long, cardIds: List<Long>, shuffleCards: Boolean): LearnCourseEntity {
        var learnCourse: LearnCourseEntity = getTempCourse()
        val date = DateUtils.getCurrentTimeDate()
        learnCourse.change(qPackId, cardIds, date)
        // TODO сомнительно, что это тут должно быть
        learnCourse.prepareToCardsView(shuffleCards)
        updateTempCourse(learnCourse)
        return learnCourse
    }


}