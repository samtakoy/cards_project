package ru.samtakoy.core.domain.impl

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx2.rxCompletable
import kotlinx.coroutines.rx2.rxSingle
import ru.samtakoy.R
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity.Companion.initNew
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule
import ru.samtakoy.core.data.local.database.room.entities.getNotInCards
import ru.samtakoy.core.data.local.database.room.entities.hasNotInCards
import ru.samtakoy.core.data.local.database.room.entities.hasRealizedSchedule
import ru.samtakoy.core.data.local.database.room.entities.hasRestSchedule
import ru.samtakoy.core.data.local.database.room.entities.types.CourseType
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.data.local.reps.CardsRepository
import ru.samtakoy.core.data.local.reps.CourseViewRepository
import ru.samtakoy.core.data.local.reps.CoursesRepository
import ru.samtakoy.core.domain.CoursesPlanner
import ru.samtakoy.core.domain.NCoursesInteractor
import ru.samtakoy.core.domain.utils.LearnCourseCardsIdsPair
import ru.samtakoy.core.domain.utils.MessageException
import javax.inject.Inject

class NCoursesInteractorImpl @Inject constructor(
    private val mCardsRepository: CardsRepository,
    private val mCourseViewRepository: CourseViewRepository,
    private val mCoursesRepository: CoursesRepository,
    private val mCoursesPlanner: CoursesPlanner
) : NCoursesInteractor {

    override suspend fun getCourse(courseId: Long): LearnCourseEntity? {
        return mCoursesRepository.getCourse(courseId)
    }

    override fun getCourseAsFlow(courseId: Long): Flow<LearnCourseEntity?> {
        return mCoursesRepository.getCourseAsFlow(courseId)
    }

    override fun getCourseFlowableRx(courseId: Long): Flowable<LearnCourseEntity> {
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

    override fun addCardsToCourseRx(learnCourse: LearnCourseEntity, newCardsToAdd: List<Long>): Completable {
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
        learnCourse: LearnCourseEntity,
        newCardsToAdd: List<Long?>
    ): Completable {
        return Completable.fromCallable {
            if (learnCourse.hasRealizedSchedule()) {
                mCoursesPlanner.planAdditionalCards(
                    learnCourse.qPackId,
                    learnCourse.title + "+",
                    newCardsToAdd,
                    if (learnCourse.hasRestSchedule()) {
                        Schedule(learnCourse.realizedSchedule.mItems + learnCourse.restSchedule.firstItem!!)
                    } else {
                        learnCourse.realizedSchedule
                    }
                )
            }
            true
        }
    }

    override suspend fun addCourseForQPack(courseTitle: String, qPackId: Long): LearnCourseEntity {
        val cardIds =  mCardsRepository.getCardsIdsFromQPack(qPackId)
        return addCourseForQPack(
            courseTitle,
            qPackId,
            cardIds
        )
    }

    private suspend fun addCourseForQPack(courseTitle: String, qPackId: Long, cardIds: List<Long>): LearnCourseEntity {
        return mCoursesRepository.addNewCourse(
            initNew(
                qPackId,
                CourseType.PRIMARY,
                courseTitle,
                LearnCourseMode.PREPARING,
                cardIds,
                Schedule.DEFAULT,
                null
            )
        )
    }

    override fun getAllCoursesRx(): Flowable<List<LearnCourseEntity>> {
        return mCoursesRepository.getAllCoursesRx()
    }

    override fun getAllCoursesAsFlow(): Flow<List<LearnCourseEntity>> {
        return mCoursesRepository.getAllCoursesAsFlow()
    }

    override fun getCoursesByIds(targetCourseIds: Array<Long>): Flowable<List<LearnCourseEntity>> {
        return mCoursesRepository.getCoursesByIds(targetCourseIds)
    }

    override fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourseEntity>> {
        return mCoursesRepository.getCoursesByIdsAsFlow(targetCourseIds)
    }

    override fun getCoursesByModes(targetModes: List<LearnCourseMode>): Flowable<List<LearnCourseEntity>> {
        return mCoursesRepository.getCoursesByModes(targetModes)
    }

    override fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourseEntity>> {
        return mCoursesRepository.getCoursesByModesAsFlow(targetModes)
    }

    override fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourseEntity>> {
        return mCoursesRepository.getCoursesForQPackRx(qPackId)
    }

    override fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourseEntity>> {
        return mCoursesRepository.getCoursesForQPackAsFlow(qPackId)
    }

    override suspend fun saveCourse(learnCourse: LearnCourseEntity) {
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

    override fun getCourseLastViewIdAsFlow(learnCourseId: Long): Flow<Long?> {
        return mCourseViewRepository.getCourseLastViewIdAsFlow(learnCourseId)
    }

    override suspend fun getCourseIdForViewId(viewId: Long): Long? {
        return mCourseViewRepository.getCourseIdForViewId(viewId = viewId)
    }

    override suspend fun addCourseView(courseId: Long, viewId: Long) {
        mCourseViewRepository.addCourseView(courseId = courseId, viewId = viewId)
    }

    override suspend fun addNewCourse(newCourse: LearnCourseEntity): LearnCourseEntity {
        return mCoursesRepository.addNewCourse(newCourse)
    }
}
