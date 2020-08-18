package ru.samtakoy.core.screens.courses.info

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.samtakoy.core.business.NCoursesInteractor
import ru.samtakoy.core.model.LearnCourse
import ru.samtakoy.core.model.LearnCourseMode.*
import ru.samtakoy.core.model.elements.ScheduleTimeUnit
import ru.samtakoy.core.model.utils.DateUtils
import ru.samtakoy.core.screens.cards.types.CardViewMode
import ru.samtakoy.core.screens.cards.types.CardViewSource
import javax.inject.Inject


@InjectViewState
class CourseInfoPresenter(
        val coursesInteractor: NCoursesInteractor,
        val courseId: Long
) : MvpPresenter<CourseInfoView>() {


    class Factory @Inject constructor(
            val coursesInteractor: NCoursesInteractor
    ) {

        fun create(courseId: Long) = CourseInfoPresenter(coursesInteractor, courseId)
    }

    val learnCourse: LearnCourse

    init {
        learnCourse = coursesInteractor.getCourse(courseId)

        viewState.showLearnCourseInfo(learnCourse)
    }

    // ----------------------------

    private fun startLearning() {
        learnCourse.toLearnMode()
        coursesInteractor.saveCourse(learnCourse)
        viewState.showLearnCourseInfo(learnCourse)
        goroCardsRepeating()
    }

    private fun continueLearning() {
        goroCardsRepeating()
    }

    private fun continueRepeating() {
        //mLearnCourse.prepareToCardsView();
        goroCardsRepeating()
    }

    private fun startRepeatingExtraordinaryOrNext() {
        val timeDelta = DateUtils.dateToDbSerialized(learnCourse.getRepeatDate()) - DateUtils.getCurrentTimeLong()
        if (timeDelta < ScheduleTimeUnit.MINUTE.millis) {
            startRepeating()
        } else {
            viewState.requestExtraordinaryRepeating()
        }
    }

    private fun startRepeating() {
        learnCourse.toRepeatMode()
        coursesInteractor.saveCourse(learnCourse)
        viewState.showLearnCourseInfo(learnCourse)
        goroCardsRepeating()
    }

    private fun startRepeatingExtraordinary() {

        val tempLearnCourse = coursesInteractor.getTempCourseFor(
                learnCourse.getQPackId(),
                learnCourse.getCardIds(),
                true
        )
        tempLearnCourse.toRepeatMode()

        viewState.navigateToCardsViewScreen(tempLearnCourse.id, CardViewSource.EXTRA_REPEATING, CardViewMode.REPEATING)
    }

    private fun goroCardsRepeating() {
        val viewMode = if (learnCourse.mode === LEARNING) CardViewMode.LEARNING else CardViewMode.REPEATING
        // TODO почему-то тут оказалось CardViewSource.ROUTINE_REPEATING, совсем не используется?
        viewState.navigateToCardsViewScreen(learnCourse.id, CardViewSource.ROUTINE_REPEATING, viewMode)
    }

    // ----------------------------

    fun onUiDeleteCourse() {
        coursesInteractor.deleteCourse(courseId);
        viewState.exit();
    }

    fun onUiActionButtonClick() {
        when (learnCourse.mode) {
            PREPARING, LEARN_WAITING -> startLearning()
            LEARNING -> continueLearning()
            REPEAT_WAITING -> startRepeatingExtraordinaryOrNext()
            REPEATING -> continueRepeating()
            COMPLETED -> startRepeatingExtraordinary()
        }
    }

    fun onUiStartRepeatingExtraConfirm() {
        startRepeatingExtraordinary()
    }

}