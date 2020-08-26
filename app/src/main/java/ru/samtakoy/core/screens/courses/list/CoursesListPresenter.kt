package ru.samtakoy.core.screens.courses.list

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.business.CardsInteractor
import ru.samtakoy.core.business.NCoursesInteractor
import ru.samtakoy.core.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.database.room.entities.elements.Schedule
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.screens.log.MyLog
import java.util.*
import javax.inject.Inject


@InjectViewState
class CoursesListPresenter(
        private val coursesInteractor: NCoursesInteractor,
        private val cardsInteractor: CardsInteractor,
        private val targetQPackId: Long?,
        private val targetModes: List<LearnCourseMode>?,
        private val targetCourseIds: Array<Long>?
) : MvpPresenter<CoursesListView>() {

    class Factory @Inject constructor(
            private val coursesInteractor: NCoursesInteractor,
            private val cardsInteractor: CardsInteractor
    ) {

        fun create(
                targetQPackId: Long?,
                targetModes: List<LearnCourseMode>?,
                targetCourseIds: Array<Long>?
        ) = CoursesListPresenter(
                coursesInteractor,
                cardsInteractor,
                targetQPackId, targetModes, targetCourseIds
        )
    }

    private var mNewCourseDefaultTitle: String = ""
    private val compositeDisposableForCourses: CompositeDisposable = CompositeDisposable()
    private val compositeDisposableForQPack: CompositeDisposable = CompositeDisposable()

    init {

        updateCurCourses()

        if (targetQPackId != null) {
            updateDefaultTitleFromQPackTitle()
        }
    }

    private fun updateDefaultTitleFromQPackTitle() {

        compositeDisposableForQPack.clear()
        compositeDisposableForQPack.add(
                cardsInteractor.getQPackRx(targetQPackId!!)
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .singleOrError()
                        .subscribe(
                                { t -> mNewCourseDefaultTitle = t.title },
                                { t: Throwable? -> onUpdateQpackTitleError(t) }
                        )
        )
    }

    override fun onDestroy() {

        compositeDisposableForCourses.dispose()
        compositeDisposableForQPack.dispose()

        super.onDestroy()
    }

    fun getStateToSave(): String {
        return mNewCourseDefaultTitle;
    }

    fun onRestoreState(state: String) {
        mNewCourseDefaultTitle = state;
    }

    fun hasQPack(): Boolean = targetQPackId != null

    private fun updateCurCourses() {

        compositeDisposableForCourses.clear()

        val curCourses = if (targetQPackId == null && targetModes == null && targetCourseIds == null) {
            coursesInteractor.getAllCourses()
        } else if (targetCourseIds != null) {
            coursesInteractor.getCoursesByIds(targetCourseIds)
        } else if (targetModes != null) {
            coursesInteractor.getCoursesByModes(targetModes)
        } else {
            coursesInteractor.getCoursesForQPack(targetQPackId!!)
        }

        compositeDisposableForCourses.add(
                curCourses
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { t: MutableList<LearnCourseEntity>? -> viewState.showCourses(t!!) },
                                { t: Throwable? -> onCoursesGetError(t) }
                        ))

    }

    private fun onCoursesGetError(t: Throwable?) {
        MyLog.add(ExceptionUtils.getMessage(t))
        viewState.showError(R.string.fragment_courses_courses_loading_err)
    }

    private fun addCourse(courseTitle: String) {
        // TODO почему такая уверенность?
        val qPackId: Long = targetQPackId!!;
        val newCourse = LearnCourseEntity.createNewPreparing(
                qPackId, courseTitle, LearnCourseMode.PREPARING,
                LinkedList(), Schedule.DEFAULT, java.util.Date()
        )

        compositeDisposableForCourses.clear()
        compositeDisposableForCourses.add(
                coursesInteractor.addNewCourse(newCourse)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { t -> onCourseAdded(t) },
                                { t: Throwable? -> onCourseAddError(t) }
                        )
        )

    }

    private fun onUpdateQpackTitleError(t: Throwable?) {
        MyLog.add(ExceptionUtils.getMessage(t))
        viewState.showError(R.string.fragment_courses_get_qpack_err)
    }

    private fun onCourseAddError(t: Throwable?) {
        MyLog.add(ExceptionUtils.getMessage(t))
        viewState.showError(R.string.fragment_courses_course_add_err)
    }

    private fun onCourseAdded(course: LearnCourseEntity) {
        mNewCourseDefaultTitle = ""
        updateCurCourses()
    }

    fun onUiCourseClick(course: LearnCourseEntity) = viewState.navigateToCourseInfo(course.id)
    fun onUiAddNewCourseConfirm(courseTitle: String) = addCourse(courseTitle)
    fun onUiAddCourseRequestClick() = viewState.showAddCourseDialog(mNewCourseDefaultTitle);
    fun onUiBatchExportToEmailClick() = viewState.showBatchExportToEmailDialog();


}