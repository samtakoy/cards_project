package ru.samtakoy.features.learncourse.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx2.rxCompletable
import kotlinx.coroutines.rx2.rxSingle
import ru.samtakoy.R
import ru.samtakoy.features.learncourse.domain.utils.getNotInCards
import ru.samtakoy.features.learncourse.domain.utils.hasNotInCards
import ru.samtakoy.features.learncourse.domain.utils.hasRealizedSchedule
import ru.samtakoy.features.learncourse.domain.utils.hasRestSchedule
import ru.samtakoy.features.card.data.CardsRepository
import ru.samtakoy.features.learncourseview.data.CourseViewRepository
import ru.samtakoy.features.learncourse.data.CoursesRepository
import ru.samtakoy.core.domain.utils.MessageException
import ru.samtakoy.features.learncourse.domain.model.CourseType
import ru.samtakoy.features.learncourse.domain.model.LearnCourse
import ru.samtakoy.features.learncourse.domain.model.LearnCourseMode
import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule
import java.util.Date
import javax.inject.Inject

class NCoursesInteractorImpl @Inject constructor(
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

    override fun getCourseFlowableRx(courseId: Long): Flowable<LearnCourse> {
        return mCoursesRepository.getCourseFlowableRx(courseId)
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
            throw MessageException(R.string.msg_there_is_no_new_cards_to_learn)
        }
        addCardsToCourseRx(
            learnCourse = learnCourse,
            newCardsToAdd = learnCourse.getNotInCards(cardIds)
        )
    }

    override fun addCardsToCourseRx(learnCourse: LearnCourse, newCardsToAdd: List<Long>): Completable {
        return rxCompletable {
            // TODO проверить - происходит ли добавление, 2) добавляется ли в список активного курса (в процессе повтороения)
            mCoursesRepository.updateCourse(
                learnCourse.copy(
                    cardIds = learnCourse.cardIds + newCardsToAdd
                )
            )
            true
        }.andThen( // additional
            // TODO надо спрашивать пользователя отдельно после добавления карточек курс
            planAdditionalCourseAfterCardsAdding(learnCourse, newCardsToAdd)
        )
    }

    private fun planAdditionalCourseAfterCardsAdding(
        learnCourse: LearnCourse,
        newCardsToAdd: List<Long>
    ): Completable {
        return Completable.fromCallable {
            if (learnCourse.hasRealizedSchedule()) {
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
            }
            true
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

    override fun getAllCoursesRx(): Flowable<List<LearnCourse>> {
        return mCoursesRepository.getAllCoursesRx()
    }

    override fun getAllCoursesAsFlow(): Flow<List<LearnCourse>> {
        return mCoursesRepository.getAllCoursesAsFlow()
    }

    override fun getCoursesByIds(targetCourseIds: Array<Long>): Flowable<List<LearnCourse>> {
        return mCoursesRepository.getCoursesByIds(targetCourseIds)
    }

    override fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourse>> {
        return mCoursesRepository.getCoursesByIdsAsFlow(targetCourseIds)
    }

    override fun getCoursesByModes(targetModes: List<LearnCourseMode>): Flowable<List<LearnCourse>> {
        return mCoursesRepository.getCoursesByModes(targetModes)
    }

    override fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourse>> {
        return mCoursesRepository.getCoursesByModesAsFlow(targetModes)
    }

    override fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourse>> {
        return mCoursesRepository.getCoursesForQPackRx(qPackId)
    }

    override fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourse>> {
        return mCoursesRepository.getCoursesForQPackAsFlow(qPackId)
    }

    override suspend fun saveCourse(learnCourse: LearnCourse) {
        mCoursesRepository.updateCourse(learnCourse)
    }

    override fun getCourseViewIdRx(learnCourseId: Long): Single<Long> {
        return rxSingle {
            mCourseViewRepository.getCourseLastViewId(learnCourseId)!!
        }
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

    override suspend fun addNewCourse(
        qPackId: Long,
        courseType: CourseType,
        title: String,
        mode: LearnCourseMode,
        cardIds: List<Long>?,
        restSchedule: Schedule?,
        repeatDate: Date?
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
