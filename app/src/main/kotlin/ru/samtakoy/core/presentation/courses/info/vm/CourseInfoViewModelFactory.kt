package ru.samtakoy.core.presentation.courses.info.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.features.learncourse.domain.CourseProgressUseCase
import ru.samtakoy.features.learncourse.domain.CoursesPlanner
import ru.samtakoy.features.learncourse.domain.NCoursesInteractor
import ru.samtakoy.features.views.domain.ViewHistoryInteractor

internal class CourseInfoViewModelFactory @AssistedInject constructor(
    private val coursesInteractor: NCoursesInteractor,
    private val courseProgressUseCase: CourseProgressUseCase,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val coursesPlanner: CoursesPlanner,
    private val viewStateMapper: CourseInfoViewStateMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val learnCourseId: Long
): AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == CourseInfoViewModelImpl::class)
        return CourseInfoViewModelImpl(
            coursesInteractor = coursesInteractor,
            courseProgressUseCase = courseProgressUseCase,
            viewHistoryInteractor = viewHistoryInteractor,
            coursesPlanner = coursesPlanner,
            viewStateMapper = viewStateMapper,
            resources = resources,
            scopeProvider = scopeProvider,
            learnCourseId = learnCourseId,
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(learnCourseId: Long): CourseInfoViewModelFactory
    }
}