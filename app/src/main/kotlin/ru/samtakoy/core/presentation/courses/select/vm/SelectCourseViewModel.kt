package ru.samtakoy.core.presentation.courses.select.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.courses.model.CourseItemUiModel
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModel.Action
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModel.Event
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModel.State

@Immutable
interface SelectCourseViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val curCourses: ImmutableList<CourseItemUiModel>
    )

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        class ExitOk(val courseId: Long) : Action
        object ExitCanceled : Action
    }

    sealed interface Event {
        class CourseClick(val item: CourseItemUiModel) : Event
    }
}