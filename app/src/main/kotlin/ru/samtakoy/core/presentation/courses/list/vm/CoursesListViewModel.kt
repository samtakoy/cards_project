package ru.samtakoy.core.presentation.courses.list.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.Action
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.Event
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.State
import ru.samtakoy.core.presentation.courses.model.CourseItemUiModel

@Immutable
internal interface CoursesListViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val isLoading: Boolean,
        val isMenuItemAddVisible: Boolean,
        val curCourses: ImmutableList<CourseItemUiModel>
    )

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        class ShowAddCourseDialog(val defaultTitle: String) : Action
        object ShowBatchExportToEmailDialog : Action
    }

    sealed interface NavigationAction : Action {
        class NavigateToCourseInfo(val courseId: Long) : NavigationAction
    }

    sealed interface Event {
        class CourseClick(val course: CourseItemUiModel) : Event
        class AddNewCourseConfirm(val courseTitle: String?) : Event
        object AddCourseRequestClick : Event
        object BatchExportToEmailClick : Event
    }
}