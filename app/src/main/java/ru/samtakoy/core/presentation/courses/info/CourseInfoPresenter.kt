package ru.samtakoy.core.presentation.courses.info

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.InjectViewState
import moxy.MvpPresenter
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.business.CoursesPlanner
import ru.samtakoy.core.business.NCoursesInteractor
import ru.samtakoy.core.business.utils.c_io_mainThread
import ru.samtakoy.core.business.utils.s_io_mainThread
import ru.samtakoy.core.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.database.room.entities.elements.ScheduleTimeUnit
import ru.samtakoy.core.database.room.entities.types.CourseType
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode.*
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.cards.types.CardViewSource
import ru.samtakoy.core.presentation.log.MyLog
import ru.samtakoy.core.utils.DateUtils
import javax.inject.Inject


@InjectViewState
class CourseInfoPresenter(
        val coursesInteractor: NCoursesInteractor,
        val coursesPlanner: CoursesPlanner,
        val courseId: Long
) : MvpPresenter<CourseInfoView>() {


    class Factory @Inject constructor(
            val coursesInteractor: NCoursesInteractor,
            val coursesPlanner: CoursesPlanner
    ) {

        fun create(courseId: Long) = CourseInfoPresenter(coursesInteractor, coursesPlanner, courseId)
    }

    private lateinit var learnCourse: LearnCourseEntity
    private val mOperationDisposable = CompositeDisposable()


    init {

        blockUiAndRunOpt(
                coursesInteractor.getCourse(courseId)
                        .compose(s_io_mainThread())
                        .subscribe(
                                { learnCourse ->
                                    unblockUi()
                                    initCourse(learnCourse)
                                },
                                { t -> onGetError(t) }
                        )
        )
    }

    private fun initCourse(learnCourse: LearnCourseEntity) {
        this.learnCourse = learnCourse
        viewState.showLearnCourseInfo(learnCourse)
    }

    override fun onDestroy() {
        mOperationDisposable.dispose()
        super.onDestroy()
    }

    private fun startLearning() {
        planUncompletedTaskCheckingIfNeeded();

        learnCourse.toLearnMode()

        blockUiAndRunOpt(
                coursesInteractor.saveCourse(learnCourse)
                        .compose(c_io_mainThread())
                        .subscribe(
                                {
                                    unblockUi()
                                    viewState.showLearnCourseInfo(learnCourse)
                                    gotoCardsRepeating()
                                },
                                { t -> onGetError(t) }
                        )
        )
    }

    private fun continueLearning() {
        planUncompletedTaskCheckingIfNeeded();
        gotoCardsRepeating()
    }

    private fun continueRepeating() {
        planUncompletedTaskCheckingIfNeeded();
        gotoCardsRepeating()
    }

    private fun startRepeatingExtraordinaryOrNext() {
        val timeDelta = DateUtils.dateToDbSerialized(learnCourse.repeatDate) - DateUtils.getCurrentTimeLong()
        if (timeDelta < ScheduleTimeUnit.MINUTE.millis) {
            startRepeating()
        } else {
            viewState.requestExtraordinaryRepeating()
        }
    }

    private fun startRepeating() {

        planUncompletedTaskCheckingIfNeeded();

        learnCourse.toRepeatMode()

        blockUiAndRunOpt(
                coursesInteractor.saveCourse(learnCourse)
                        .compose(c_io_mainThread())
                        .subscribe(
                                {
                                    unblockUi()
                                    viewState.showLearnCourseInfo(learnCourse)
                                    gotoCardsRepeating()
                                },
                                { t -> onGetError(t) }
                        )
        )
    }


    private fun startRepeatingExtraordinary() {


        blockUiAndRunOpt(
                coursesInteractor.getTempCourseFor(learnCourse.qPackId, learnCourse.cardIds, true)
                        .compose(s_io_mainThread())
                        .subscribe(
                                { tempLearnCourse ->
                                    unblockUi()
                                    tempLearnCourse.toRepeatMode()
                                    viewState.navigateToCardsViewScreen(tempLearnCourse.id, CardViewSource.EXTRA_REPEATING, CardViewMode.REPEATING)
                                },
                                { t -> onGetError(t) }
                        )
        )
    }

    private fun gotoCardsRepeating() {
        val viewMode = if (learnCourse.mode === LEARNING) CardViewMode.LEARNING else CardViewMode.REPEATING
        // TODO почему-то тут оказалось CardViewSource.ROUTINE_REPEATING, совсем не используется?
        viewState.navigateToCardsViewScreen(learnCourse.id, CardViewSource.ROUTINE_REPEATING, viewMode)
    }

    private fun planUncompletedTaskCheckingIfNeeded() {
        if (learnCourse.courseType != CourseType.TEMPORARY) {
            coursesPlanner.planUncompletedTasksChecking();
        }
    }

    // ----------------------------

    fun onUiDeleteCourse() {
        blockUiAndRunOpt(
                coursesInteractor.deleteCourse(courseId)
                        .compose(c_io_mainThread())
                        .subscribe(
                                {
                                    unblockUi()
                                    viewState.exit()
                                },
                                { t -> onGetError(t) }
                        )
        )
    }

    fun onUiActionButtonClick() {
        when (learnCourse.mode) {
            PREPARING, LEARN_WAITING -> startLearning()
            LEARNING -> continueLearning()
            REPEAT_WAITING -> startRepeatingExtraordinaryOrNext()
            REPEATING -> continueRepeating()
            COMPLETED -> startRepeatingExtraordinary()
            //TEMPORARY -> return
        }
    }

    fun onUiStartRepeatingExtraConfirm() {
        startRepeatingExtraordinary()
    }


    private fun onGetError(t: Throwable) {
        unblockUi();
        MyLog.add(ExceptionUtils.getMessage(t), t);
        viewState.showError(R.string.db_request_err_message);
    }

    private fun isOperationInProgress(): Boolean {
        return mOperationDisposable.size() > 0
    }

    private fun blockUi() {
        viewState.blockScreenOnOperation()
    }

    private fun unblockUi() {
        viewState.unblockScreenOnOperation()
        mOperationDisposable.clear()
    }

    private fun blockUiAndRunOpt(disposable: Disposable) {
        if (isOperationInProgress()) {
            MyLog.add("CourseInfoPresenter: wrong ui logic")
            disposable.dispose()
            return
        }
        blockUi()
        mOperationDisposable.clear()
        mOperationDisposable.add(disposable)
    }

}