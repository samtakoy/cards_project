package ru.samtakoy.features.learncourse.domain

import ru.samtakoy.features.learncourse.domain.utils.finishLearnOrRepeat
import ru.samtakoy.features.learncourse.domain.utils.makeInitialTodosStatic
import ru.samtakoy.features.database.data.TransactionRepository
import ru.samtakoy.features.learncourse.domain.model.LearnCourse
import ru.samtakoy.features.learncourse.domain.model.LearnCourseMode
import ru.samtakoy.features.qpack.domain.QPackInteractor
import ru.samtakoy.features.views.domain.ViewHistoryInteractor
import ru.samtakoy.features.views.domain.ViewHistoryItem
import java.util.Date
import javax.inject.Inject

class CourseProgressUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val coursesInteractor: NCoursesInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val qPackInteractor: QPackInteractor,
    private val coursesPlanner: CoursesPlanner
): CourseProgressUseCase {
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

    override suspend fun finishCourseCardsViewing(courseId: Long, currentTime: Date) {
        coursesInteractor.getCourse(courseId = courseId)?.let { course ->
            if (course.qPackId > 0) {
                qPackInteractor.updateQPackViewCount(course.qPackId, currentTime)
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