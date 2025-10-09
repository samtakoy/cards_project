package ru.samtakoy.core.presentation.courses.select.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.features.learncourse.domain.NCoursesInteractor
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapper

internal class SelectCourseViewModelFactory @AssistedInject constructor(
    private val coursesInteractor: NCoursesInteractor,
    private val itemsMapper: CourseItemUiMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val targetQPackId: Long?
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == SelectCourseViewModel::class.java)
        @Suppress("UNCHECKED_CAST")
        return SelectCourseViewModelImpl(
            coursesInteractor = coursesInteractor,
            itemsMapper = itemsMapper,
            resources = resources,
            savedStateHandle = handle,
            scopeProvider = scopeProvider,
            targetQPackId = targetQPackId
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            targetQPackId: Long?
        ): SelectCourseViewModelFactory
    }
}