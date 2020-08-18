package ru.samtakoy.core.screens.courses.select

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.business.NCoursesInteractor
import ru.samtakoy.core.model.LearnCourse
import ru.samtakoy.core.model.QPack
import ru.samtakoy.core.screens.log.MyLog
import javax.inject.Inject

@InjectViewState
class SelectCoursePresenter(
        private val coursesInteractor: NCoursesInteractor,
        targetQPack: QPack?
) : MvpPresenter<SelectCourseView>() {


    class Factory @Inject constructor(
            private val coursesInteractor: NCoursesInteractor
    ) {

        fun create(
                targetQPack: QPack?
        ) = SelectCoursePresenter(coursesInteractor, targetQPack)
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {

        updateCurCourses(targetQPack)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()

        super.onDestroy()
    }

    private fun updateCurCourses(targetQPack: QPack?) {
        val curCourses = if (targetQPack == null) {
            coursesInteractor.getAllCourses();
        } else {
            coursesInteractor.getCoursesForQPack(targetQPack.id)
        }

        compositeDisposable.clear()
        compositeDisposable.add(
                curCourses
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { t -> viewState.showCourses(t) },
                                { t: Throwable? -> onCoursesGetError(t) }
                        )
        )

    }

    private fun onCoursesGetError(t: Throwable?) {
        MyLog.add(t.toString() + "\n" + ExceptionUtils.getMessage(t))
        viewState.showError(R.string.fragment_courses_courses_loading_err)
    }

    fun onUiCourseClick(course: LearnCourse) = viewState.exitOk(course.id);


}