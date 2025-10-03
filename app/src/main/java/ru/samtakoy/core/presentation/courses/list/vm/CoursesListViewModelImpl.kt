package ru.samtakoy.core.presentation.courses.list.vm

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.utils.DateUtils
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule
import ru.samtakoy.core.data.local.database.room.entities.types.CourseType
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.domain.NCoursesInteractor
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.Action
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.Event
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.NavigationAction
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.State
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapper
import ru.samtakoy.core.presentation.courses.model.CourseItemUiModel
import ru.samtakoy.core.presentation.log.MyLog
import java.util.LinkedList

internal class CoursesListViewModelImpl(
    private val coursesInteractor: NCoursesInteractor,
    private val cardsInteractor: CardsInteractor,
    private val courseItemsMapper: CourseItemUiMapper,
    private val resources: Resources,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    private val targetQPackId: Long?,
    private val targetModes: List<LearnCourseMode>?,
    private val targetCourseIds: Array<Long>?
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        isLoading = false,
        isMenuItemAddVisible = targetQPackId != null && targetQPackId != 0L,
        curCourses = emptyList<CourseItemUiModel>().toImmutableList()
    )
), CoursesListViewModel {

    private val dataState = SavedStateValue<DataState>(
        initialValueGetter = { DataState(newCourseDefaultTitle = "", isSaved = false) },
        keyName = KEY_DATA_STATE,
        savedStateHandle = savedStateHandle,
        serialize = { it },
        deserialize = { it as DataState },
        saveScope = ioScope
    )

    init {
        subscribeData()
        launchWithLoader(
            onError = ::onQPackGetError
        ) {
            if (dataState.value.isSaved.not() && targetQPackId != null) {
                dataState.value = dataState.value.copy(
                    newCourseDefaultTitle = cardsInteractor.getQPack(targetQPackId)?.title.orEmpty()
                )
            }
            dataState.value = dataState.value.copy(isSaved = true)
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.AddCourseRequestClick -> onUiAddCourseRequestClick()
            is Event.AddNewCourseConfirm -> onUiAddNewCourseConfirm(event.courseTitle)
            Event.BatchExportToEmailClick -> onUiBatchExportToEmailClick()
            is Event.CourseClick -> onUiCourseClick(event.course)
        }
    }

    private fun onUiCourseClick(course: CourseItemUiModel) {
        sendAction(NavigationAction.NavigateToCourseInfo(courseId = course.id.value))
    }

    private fun onUiAddNewCourseConfirm(courseTitle: String?) {
        if (courseTitle != null && courseTitle.length > 0) {
            addCourse(courseTitle)
        }
    }

    private fun onUiAddCourseRequestClick() {
        sendAction(
            Action.ShowAddCourseDialog(dataState.value.newCourseDefaultTitle)
        )
    }

    private fun onUiBatchExportToEmailClick() {
        sendAction(Action.ShowBatchExportToEmailDialog)
    }

    private fun addCourse(courseTitle: String) {
        val qPackId: Long = targetQPackId!!
        val newCourse = LearnCourseEntity.initNew(
            qPackId = qPackId,
            courseType = CourseType.PRIMARY,
            title = courseTitle,
            mode = LearnCourseMode.PREPARING,
            cardIds = LinkedList(),
            restSchedule = Schedule.DEFAULT,
            repeatDate = DateUtils.getCurrentTimeDate()
        )

        launchWithLoader(
            onError = ::onCourseAddError
        ) {
            coursesInteractor.addNewCourse(newCourse)
            dataState.value = dataState.value.copy(
                newCourseDefaultTitle = ""
            )
        }
    }

    private fun subscribeData() {
        val curCoursesAsFlow = if (targetQPackId == null && targetModes == null && targetCourseIds == null) {
            coursesInteractor.getAllCoursesAsFlow()
        } else if (targetCourseIds != null) {
            coursesInteractor.getCoursesByIdsAsFlow(targetCourseIds)
        } else if (targetModes != null) {
            coursesInteractor.getCoursesByModesAsFlow(targetModes)
        } else {
            coursesInteractor.getCoursesForQPackAsFlow(targetQPackId!!)
        }
        curCoursesAsFlow
            .catch { onCoursesGetError(it) }
            .onEach {
                viewState = viewState.copy(curCourses = courseItemsMapper.map(it).toImmutableList())
            }
            .launchIn(mainScope)

    }

    private fun onQPackGetError(t: Throwable) {
        MyLog.add(ExceptionUtils.getMessage(t), t)
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.fragment_courses_get_qpack_err))
        )
    }

    private fun onCoursesGetError(t: Throwable?) {
        MyLog.add(ExceptionUtils.getMessage(t))
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.fragment_courses_courses_loading_err))
        )
    }

    private fun onCourseAddError(t: Throwable?) {
        MyLog.add(ExceptionUtils.getMessage(t))
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.fragment_courses_course_add_err))
        )
    }

    private fun launchWithLoader(
        onError: (suspend (Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        viewState = viewState.copy(isLoading = true)
        launchCatching(
            onError = onError,
            onFinally = { viewState = viewState.copy(isLoading = false) }
        ) {
            block()
        }
    }

    @Parcelize
    private data class DataState(
        val newCourseDefaultTitle: String,
        val isSaved: Boolean
    ) : Parcelable

    companion object {
        private const val KEY_DATA_STATE = "KEY_DATA_STATE"
    }
}