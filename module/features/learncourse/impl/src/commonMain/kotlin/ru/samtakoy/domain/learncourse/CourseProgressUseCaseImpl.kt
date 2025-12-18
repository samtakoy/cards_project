package ru.samtakoy.domain.learncourse

import ru.samtakoy.domain.common.transaction.TransactionRepository
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.domain.view.ViewHistoryItem
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal class CourseProgressUseCaseImpl(
    private val transactionRepository: TransactionRepository,
    private val coursesInteractor: NCoursesInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val coursesPlanner: CoursesPlanner
): CourseProgressUseCase {

    @OptIn(ExperimentalTime::class)
    override suspend fun startLearning(learnCourse: LearnCourse): ViewHistoryItem {
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

    @OptIn(ExperimentalTime::class)
    override suspend fun startRepeating(learnCourse: LearnCourse): ViewHistoryItem {
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

    @OptIn(ExperimentalTime::class)
    override suspend fun finishCourseCardsViewing(courseId: Long, currentTime: Instant) {
        coursesInteractor.getCourse(courseId = courseId)?.let { course ->
            coursesInteractor.saveCourse(
                course.finishLearnOrRepeat(currentTime)
            )
            // перепланировать следующий курс
            coursesPlanner.reScheduleLearnCourses()
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun finishCourseCardsViewingForViewId(viewId: Long, currentTime: Instant) {
        val courseId = coursesInteractor.getCourseIdForViewId(viewId = viewId) ?: return
        finishCourseCardsViewing(
            courseId = courseId,
            currentTime = currentTime
        )
    }
}