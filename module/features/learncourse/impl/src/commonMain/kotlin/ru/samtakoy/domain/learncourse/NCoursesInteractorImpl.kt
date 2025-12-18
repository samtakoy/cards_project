package ru.samtakoy.domain.learncourse

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.card.CardsRepository
import ru.samtakoy.domain.learncourse.schedule.Schedule
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal class NCoursesInteractorImpl(
    private val mCardsRepository: CardsRepository,
    private val mCourseViewRepository: CourseViewRepository,
    private val mCoursesRepository: CoursesRepository,
    private val mCoursesPlanner: CoursesPlanner
) : NCoursesInteractor {

    override suspend fun getCourse(courseId: Long): LearnCourse? {
        return mCoursesRepository.getCourse(courseId)
    }

    override fun getCourseAsFlow(courseId: Long): Flow<LearnCourse?> {
        return mCoursesRepository.getCourseAsFlow(courseId)
    }

    override suspend fun deleteCourse(courseId: Long) {
        return mCoursesRepository.deleteCourse(courseId)
    }

    override suspend fun deleteQPackCourses(qPackId: Long) {
        return mCoursesRepository.deleteQPackCourses(qPackId)
    }

    override suspend fun onAddCardsToCourseFromQPack(qPackId: Long, learnCourseId: Long) {
        val learnCourse = mCoursesRepository.getCourse(learnCourseId)!!
        val cardIds = mCardsRepository.getCardsIdsFromQPack(qPackId)
        if (!learnCourse.hasNotInCards(cardIds)) {
            // TODO
            throw Exception("todo")
        }
        addCardsToCourse(
            learnCourse = learnCourse,
            newCardsToAdd = learnCourse.getNotInCards(cardIds)
        )
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun addCardsToCourse(
        learnCourse: LearnCourse,
        newCardsToAdd: List<Long>
    ) {
        // TODO проверить - происходит ли добавление, 2) добавляется ли в список активного курса (в процессе повтороения)
        mCoursesRepository.updateCourse(
            learnCourse.copy(
                cardIds = learnCourse.cardIds + newCardsToAdd
            )
        )
        planAdditionalCourseAfterCardsAdding(learnCourse, newCardsToAdd)
    }

    private suspend fun planAdditionalCourseAfterCardsAdding(
        learnCourse: LearnCourse,
        newCardsToAdd: List<Long>
    ): Boolean {
        return if (learnCourse.hasRealizedSchedule()) {
            mCoursesPlanner.planAdditionalCards(
                learnCourse.qPackId,
                learnCourse.title + "+",
                newCardsToAdd,
                if (learnCourse.hasRestSchedule()) {
                    Schedule(learnCourse.realizedSchedule.items + learnCourse.restSchedule.firstItem!!)
                } else {
                    learnCourse.realizedSchedule
                }
            )
            true
        } else {
            false
        }
    }

    override suspend fun addCourseForQPack(courseTitle: String, qPackId: Long): LearnCourse {
        val cardIds =  mCardsRepository.getCardsIdsFromQPack(qPackId)
        return addCourseForQPack(
            courseTitle,
            qPackId,
            cardIds
        )
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun addCourseForQPack(courseTitle: String, qPackId: Long, cardIds: List<Long>): LearnCourse {
        return mCoursesRepository.addNewCourse(
            qPackId = qPackId,
            courseType = CourseType.PRIMARY,
            title = courseTitle,
            mode = LearnCourseMode.PREPARING,
            cardIds = cardIds,
            restSchedule = Schedule.DEFAULT,
            repeatDate = null
        )
    }

    override fun getAllCoursesAsFlow(): Flow<List<LearnCourse>> {
        return mCoursesRepository.getAllCoursesAsFlow()
    }

    override fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourse>> {
        return mCoursesRepository.getCoursesByIdsAsFlow(targetCourseIds)
    }

    override fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourse>> {
        return mCoursesRepository.getCoursesByModesAsFlow(targetModes)
    }

    override fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourse>> {
        return mCoursesRepository.getCoursesForQPackAsFlow(qPackId)
    }

    override suspend fun saveCourse(learnCourse: LearnCourse) {
        mCoursesRepository.updateCourse(learnCourse)
    }

    override suspend fun getCourseViewId(learnCourseId: Long): Long? {
        return mCourseViewRepository.getCourseLastViewId(learnCourseId)
    }

    override fun getCourseLastViewIdAsFlow(learnCourseId: Long): Flow<Long> {
        return mCourseViewRepository.getCourseLastViewIdAsFlow(learnCourseId)
    }

    override suspend fun getCourseIdForViewId(viewId: Long): Long? {
        return mCourseViewRepository.getCourseIdForViewId(viewId = viewId)
    }

    override suspend fun addCourseView(courseId: Long, viewId: Long) {
        mCourseViewRepository.addCourseView(courseId = courseId, viewId = viewId)
    }

    override suspend fun addNewCourse(newCourse: LearnCourse): LearnCourse {
        return mCoursesRepository.addNewCourse(newCourse)
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun addNewCourse(
        qPackId: Long,
        courseType: CourseType,
        title: String,
        mode: LearnCourseMode,
        cardIds: List<Long>?,
        restSchedule: Schedule?,
        repeatDate: Instant?
    ): LearnCourse {
        return addNewCourse(
            qPackId = qPackId,
            courseType = courseType,
            title = title,
            mode = mode,
            cardIds = cardIds,
            restSchedule = restSchedule,
            repeatDate = repeatDate
        )
    }
}
