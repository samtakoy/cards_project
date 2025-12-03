package ru.samtakoy.oldlegacy.core.presentation.courses.select.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.learncourse.NCoursesInteractor
import ru.samtakoy.oldlegacy.core.presentation.courses.model.CourseItemUiMapper
import ru.samtakoy.oldlegacy.core.presentation.courses.model.CourseItemUiModel
import ru.samtakoy.common.utils.log.MyLog
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl

class SelectCourseViewModelImpl(
    private val coursesInteractor: NCoursesInteractor,
    private val itemsMapper: CourseItemUiMapper,
    private val resources: Resources,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    targetQPackId: Long?
) : BaseViewModelImpl<SelectCourseViewModel.State, SelectCourseViewModel.Action, SelectCourseViewModel.Event>(
    scopeProvider = scopeProvider,
    initialState = SelectCourseViewModel.State(
        curCourses = emptyList<CourseItemUiModel>().toImmutableList()
    )
), SelectCourseViewModel {

    init {
        subscribeData(targetQPackId)
    }

    override fun onEvent(event: SelectCourseViewModel.Event) {
        when (event) {
            is SelectCourseViewModel.Event.CourseClick -> sendAction(SelectCourseViewModel.Action.ExitOk(courseId = event.item.id.value))
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
            SelectCourseViewModel.Action.ShowErrorMessage(resources.getString(R.string.db_request_err_message))
        )
    }
}