package ru.samtakoy.core.screens.courses.list

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.business.NCoursesInteractor
import ru.samtakoy.core.model.LearnCourse
import ru.samtakoy.core.model.LearnCourseMode
import ru.samtakoy.core.model.QPack
import ru.samtakoy.core.model.elements.Schedule
import ru.samtakoy.core.screens.log.MyLog
import java.sql.Date
import java.util.*
import javax.inject.Inject


@InjectViewState
class CoursesListPresenter(
        private val coursesInteractor: NCoursesInteractor,
        private var targetQPack: QPack?,
        private val targetModes: List<LearnCourseMode>?,
        private val targetCourseIds: Array<Long>?
) : MvpPresenter<CoursesListView>() {

    class Factory @Inject constructor(
            private val coursesInteractor: NCoursesInteractor
    ) {

        fun create(
                targetQPack: QPack?,
                targetModes: List<LearnCourseMode>?,
                targetCourseIds: Array<Long>?
        ) = CoursesListPresenter(
                coursesInteractor,
                targetQPack, targetModes, targetCourseIds
        )
    }

    private var mNewCourseDefaultTitle: String
    private val compositeDisposableForCourses: CompositeDisposable

    init {
        mNewCourseDefaultTitle = targetQPack?.title ?: ""
        compositeDisposableForCourses = CompositeDisposable();

        updateCurCourses()
    }

    override fun onDestroy() {

        compositeDisposableForCourses.dispose()

        super.onDestroy()
    }

    fun getStateToSave(): String {
        return mNewCourseDefaultTitle;
    }

    fun onRestoreState(state: String) {
        mNewCourseDefaultTitle = state;
    }

    fun hasQPack(): Boolean = targetQPack != null

    private fun updateCurCourses() {

        compositeDisposableForCourses.clear()

        val curCourses = if (targetQPack == null && targetModes == null && targetCourseIds == null) {
            coursesInteractor.getAllCourses()
        } else if (targetCourseIds != null) {
            coursesInteractor.getCoursesByIds(targetCourseIds)
        } else if (targetModes != null) {
            coursesInteractor.getCoursesByModes(targetModes)
        } else {
            coursesInteractor.getCoursesForQPack(targetQPack!!.id)
        }

        compositeDisposableForCourses.add(
                curCourses
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { t: MutableList<LearnCourse>? -> viewState.showCourses(t!!) },
                                { t: Throwable? -> onCoursesGetError(t) }
                        ))

    }

    private fun onCoursesGetError(t: Throwable?) {
        MyLog.add(ExceptionUtils.getMessage(t))
        viewState.showError(R.string.fragment_courses_courses_loading_err)
    }

    private fun addCourse(courseTitle: String) {
        val qPackId: Long = targetQPack!!.id;
        val newCourse = LearnCourse.createNewPreparing(
                qPackId, courseTitle, LearnCourseMode.PREPARING,
                LinkedList(), Schedule.DEFAULT, Date(0)
        )

        compositeDisposableForCourses.clear()
        compositeDisposableForCourses.add(
                coursesInteractor.addNewCourse(newCourse)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { t -> onCourseAdded(t) },
                                { t: Throwable? -> onCourseAddError(t) }
                        )
        )

    }

    private fun onCourseAddError(t: Throwable?) {
        MyLog.add(ExceptionUtils.getMessage(t))
        viewState.showError(R.string.fragment_courses_course_add_err)
    }

    private fun onCourseAdded(course: LearnCourse) {
        mNewCourseDefaultTitle = ""
        updateCurCourses()
    }

    fun onUiCourseClick(course: LearnCourse) = viewState.navigateToCourseInfo(course.id)
    fun onUiAddNewCourseConfirm(courseTitle: String) = addCourse(courseTitle)
    fun onUiAddCourseRequestClick() = viewState.showAddCourseDialog(mNewCourseDefaultTitle);
    fun onUiBatchExportToEmailClick() = viewState.showBatchExportToEmailDialog();


}