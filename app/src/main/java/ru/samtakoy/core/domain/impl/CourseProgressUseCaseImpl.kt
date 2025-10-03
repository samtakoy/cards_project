package ru.samtakoy.core.domain.impl

import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.finishLearnOrRepeat
import ru.samtakoy.core.data.local.database.room.entities.makeInitialTodosStatic
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.data.local.reps.TransactionRepository
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.domain.CourseProgressUseCase
import ru.samtakoy.core.domain.CoursesPlanner
import ru.samtakoy.core.domain.NCoursesInteractor
import ru.samtakoy.features.views.domain.ViewHistoryInteractor
import ru.samtakoy.features.views.domain.ViewHistoryItem
import java.util.Date
import javax.inject.Inject

class CourseProgressUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val coursesInteractor: NCoursesInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val cardsInteractor: CardsInteractor,
    private val coursesPlanner: CoursesPlanner
): CourseProgressUseCase {
    override suspend fun startLearning(learnCourse: LearnCourseEntity): ViewHistoryItem {
        return transactionRepository.withTransaction<ViewHistoryItem> {

            // меняем курс
            coursesInteractor.saveCourse(
                learnCourse.copy(mode = LearnCourseMode.LEARNING)
            )
            // добавляем новый просмотр
            val viewItem = viewHistoryInteractor.addNewViewItem(
                qPackId = learnCourse.qPackId,
                cardIds = makeInitialTodosStatic(cardIds = learnCourse.cardIds, shuffleCards = false)
            )

            // связываем с курсом
            coursesInteractor.addCourseView(
                courseId = learnCourse.id,
                viewId = viewItem.id
            )

            viewItem
        }
    }

    override suspend fun startRepeating(learnCourse: LearnCourseEntity): ViewHistoryItem {
        return transactionRepository.withTransaction<ViewHistoryItem> {
            // меняем курс
            coursesInteractor.saveCourse(
                learnCourse.copy(mode = LearnCourseMode.REPEATING)
            )

            // добавляем новый просмотр
            val viewItem = viewHistoryInteractor.addNewViewItem(
                qPackId = learnCourse.qPackId,
                cardIds = makeInitialTodosStatic(cardIds = learnCourse.cardIds, shuffleCards = true)
            )

            // связываем с курсом
            coursesInteractor.addCourseView(
                courseId = learnCourse.id,
                viewId = viewItem.id
            )

            viewItem
        }
    }

    override suspend fun finishCourseCardsViewing(courseId: Long, currentTime: Date) {
        coursesInteractor.getCourse(courseId = courseId)?.let { course ->
            if (course.qPackId > 0) {
                cardsInteractor.updateQPackViewCount(course.qPackId, currentTime)
            }
            coursesInteractor.saveCourse(
                course.finishLearnOrRepeat(currentTime)
            )
            // перепланировать следующий курс
            coursesPlanner.reScheduleLearnCourses()
        }
    }

    override suspend fun finishCourseCardsViewingForViewId(viewId: Long, currentTime: Date) {
        val courseId = coursesInteractor.getCourseIdForViewId(viewId = viewId) ?: return
        finishCourseCardsViewing(
            courseId = courseId,
            currentTime = currentTime
        )
    }
}