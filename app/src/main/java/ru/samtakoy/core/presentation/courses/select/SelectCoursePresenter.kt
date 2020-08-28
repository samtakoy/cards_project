package ru.samtakoy.core.presentation.courses.select

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.business.NCoursesInteractor
import ru.samtakoy.core.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.presentation.log.MyLog
import javax.inject.Inject

@InjectViewState
class SelectCoursePresenter(
        private val coursesInteractor: NCoursesInteractor,
        targetQPackId: Long?
) : MvpPresenter<SelectCourseView>() {


    class Factory @Inject constructor(
            private val coursesInteractor: NCoursesInteractor
    ) {

        fun create(
                targetQPackId: Long?
        ) = SelectCoursePresenter(coursesInteractor, targetQPackId)
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {

        bindCourses(targetQPackId)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()

        super.onDestroy()
    }

    private fun bindCourses(targetQPackId: Long?) {
        val curCourses = if (targetQPackId == null) {
            coursesInteractor.getAllCourses();
        } else {
            coursesInteractor.getCoursesForQPack(targetQPackId)
        }

        compositeDisposable.clear()
        compositeDisposable.add(
                curCourses
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { t -> onCoursesReceived(t) },
                                { t: Throwable? -> onCoursesGetError(t) }
                        )
        )

    }

    private fun onCoursesReceived(coursesList: List<LearnCourseEntity>) {
        // TODO DiffUtil (тут или в ui)
        viewState.showCourses(coursesList);
    }

    private fun onCoursesGetError(t: Throwable?) {
        MyLog.add(t.toString() + "\n" + ExceptionUtils.getMessage(t))
        viewState.showError(R.string.fragment_courses_courses_loading_err)
    }

    fun onUiCourseClick(course: LearnCourseEntity) = viewState.exitOk(course.id);


}