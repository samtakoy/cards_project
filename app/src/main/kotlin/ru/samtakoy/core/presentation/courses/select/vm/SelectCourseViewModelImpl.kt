package ru.samtakoy.core.presentation.courses.select.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.features.learncourse.domain.NCoursesInteractor
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapper
import ru.samtakoy.core.presentation.courses.model.CourseItemUiModel
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModel.Action
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModel.Event
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModel.State
import ru.samtakoy.core.presentation.log.MyLog

class SelectCourseViewModelImpl(
    private val coursesInteractor: NCoursesInteractor,
    private val itemsMapper: CourseItemUiMapper,
    private val resources: Resources,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    targetQPackId: Long?
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        curCourses = emptyList<CourseItemUiModel>().toImmutableList()
    )
), SelectCourseViewModel {

    init {
        subscribeData(targetQPackId)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.CourseClick -> sendAction(Action.ExitOk(courseId = event.item.id.value))
        }
    }

    private fun subscribeData(targetQPackId: Long?) {
        if (targetQPackId == null) {
            coursesInteractor.getAllCoursesAsFlow()
        } else {
            coursesInteractor.getCoursesForQPackAsFlow(targetQPackId)
        }
            .catch { onGetError(it) }
            .onEach {
                viewState = viewState.copy(
                    curCourses = itemsMapper.map(it).toImmutableList()
                )
            }
            .launchIn(mainScope)
    }

    private fun onGetError(t: Throwable) {
        MyLog.add(ExceptionUtils.getMessage(t), t)
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.db_request_err_message))
        )
    }
}