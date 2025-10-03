package ru.samtakoy.core.presentation.courses.list.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.domain.NCoursesInteractor
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapper

internal class CoursesListViewModelFactory @AssistedInject constructor(
    private val coursesInteractor: NCoursesInteractor,
    private val cardsInteractor: CardsInteractor,
    private val courseItemsMapper: CourseItemUiMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val targetQPackId: Long?,
    @Assisted
    private val targetModes: List<LearnCourseMode>?,
    @Assisted
    private val targetCourseIds: Array<Long>?
) : AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == CoursesListViewModelImpl::class.java)
        @Suppress("UNCHECKED_CAST")
        return CoursesListViewModelImpl(
            coursesInteractor = coursesInteractor,
            cardsInteractor = cardsInteractor,
            courseItemsMapper = courseItemsMapper,
            resources = resources,
            savedStateHandle = handle,
            scopeProvider = scopeProvider,
            targetQPackId = targetQPackId,
            targetModes = targetModes,
            targetCourseIds = targetCourseIds
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            targetQPackId: Long?,
            targetModes: List<LearnCourseMode>?,
            targetCourseIds: Array<Long>?
        ): CoursesListViewModelFactory
    }
}