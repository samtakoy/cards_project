package ru.samtakoy.core.presentation.courses.info.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.utils.DateUtils
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.elements.ScheduleTimeUnit
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode.COMPLETED
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode.LEARNING
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode.LEARN_WAITING
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode.PREPARING
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode.REPEATING
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode.REPEAT_WAITING
import ru.samtakoy.core.domain.CourseProgressUseCase
import ru.samtakoy.core.domain.CoursesPlanner
import ru.samtakoy.core.domain.NCoursesInteractor
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewModel.Action
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewModel.Event
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewModel.NavigationAction
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewModel.State
import ru.samtakoy.core.presentation.log.MyLog
import ru.samtakoy.features.views.domain.ViewHistoryInteractor
import ru.samtakoy.features.views.domain.ViewHistoryItem

internal class CourseInfoViewModelImpl(
    private val coursesInteractor: NCoursesInteractor,
    private val courseProgressUseCase: CourseProgressUseCase,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val coursesPlanner: CoursesPlanner,
    private val viewStateMapper: CourseInfoViewStateMapper,
    private val resources: Resources,
    scopeProvider: ScopeProvider,
    private val learnCourseId: Long
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        type = State.Type.Initialization,
        isLoading = false
    )
), CourseInfoViewModel {

    private val dataStateAsFlow = MutableStateFlow<DataState?>(null)

    init {
        subscribeData()
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.ActionButtonClick -> onUiActionButtonClick()
            Event.DeleteCourseClick -> onUiDeleteCourse()
            Event.StartRepeatingExtraConfirm -> onUiStartRepeatingExtraConfirm()
        }
    }

    private fun onUiDeleteCourse() {
        launchWithLoader {
            coursesInteractor.deleteCourse(learnCourseId)
            sendAction(NavigationAction.Exit)
        }
    }

    private fun onUiActionButtonClick() {
        val learnCourse = dataStateAsFlow.value?.learnCourse ?: return
        when (learnCourse.mode) {
            PREPARING, LEARN_WAITING -> startLearning()
            LEARNING -> continueLearning()
            REPEAT_WAITING -> startRepeatingExtraordinaryOrNext()
            REPEATING -> continueRepeating()
            COMPLETED -> startRepeatingExtraordinary()
        }
    }

    private fun onUiStartRepeatingExtraConfirm() {
        startRepeatingExtraordinary()
    }

    private fun startLearning() {
        val learnCourse = dataStateAsFlow.value?.learnCourse ?: return
        planUncompletedTaskCheckingIfNeeded()

        launchWithLoader {
            val viewItem = courseProgressUseCase.startLearning(learnCourse)
            gotoCardsRepeating(viewItem.id)
        }
    }

    private fun continueLearning() {
        val learnCourse = dataStateAsFlow.value?.learnCourse ?: return
        planUncompletedTaskCheckingIfNeeded()

        launchWithLoader {
            val viewItemId = coursesInteractor.getCourseViewId(learnCourse.id)!!
            gotoCardsRepeating(viewItemId)
        }
    }

    private fun continueRepeating() {
        val learnCourse = dataStateAsFlow.value?.learnCourse ?: return
        planUncompletedTaskCheckingIfNeeded();

        launchWithLoader {
            val viewItemId = coursesInteractor.getCourseViewId(learnCourse.id)!!
            gotoCardsRepeating(viewItemId)
        }
    }

    private fun startRepeatingExtraordinaryOrNext() {
        val learnCourse = dataStateAsFlow.value?.learnCourse ?: return
        val timeDelta = DateUtils.dateToDbSerialized(learnCourse.repeatDate) - DateUtils.getCurrentTimeLong()
        if (timeDelta < ScheduleTimeUnit.MINUTE.millis) {
            startRepeating()
        } else {
            sendAction(Action.RequestExtraordinaryRepeating)
        }
    }

    private fun startRepeating() {
        val learnCourse = dataStateAsFlow.value?.learnCourse ?: return
        planUncompletedTaskCheckingIfNeeded();

        launchWithLoader {
            val viewItem = courseProgressUseCase.startRepeating(learnCourse)
            gotoCardsRepeating(viewItem.id)
        }
    }

    private fun startRepeatingExtraordinary() {
        val learnCourse = dataStateAsFlow.value?.learnCourse ?: return

        launchWithLoader {
            val viewHistoryItem = viewHistoryInteractor.addNewViewItemForPack(
                qPackId = learnCourse.qPackId,
                cardIds = learnCourse.cardIds,
                shuffleCards = true
            )
            sendAction(
                NavigationAction.NavigateToCardsViewScreen(
                    viewHistoryItemId = viewHistoryItem.id,
                    viewMode = CardViewMode.REPEATING
                )
            )
        }
    }

    private fun gotoCardsRepeating(viewItemId: Long) {
        val learnCourse = dataStateAsFlow.value?.learnCourse ?: return

        sendAction(
            NavigationAction.NavigateToCardsViewScreen(
                viewHistoryItemId = viewItemId,
                viewMode = if (
                    learnCourse.mode === LEARNING ||
                    learnCourse.mode === LEARN_WAITING
                ) {
                    CardViewMode.LEARNING
                } else {
                    CardViewMode.REPEATING
                }
            )
        )
    }

    private fun planUncompletedTaskCheckingIfNeeded() {
        coursesPlanner.planUncompletedTasksChecking();
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun subscribeData() {
        combine(
            coursesInteractor.getCourseAsFlow(learnCourseId)
                .distinctUntilChanged(),
            coursesInteractor.getCourseLastViewIdAsFlow(learnCourseId)
                .distinctUntilChanged()
                .mapLatest { viewId ->
                    if (viewId == null) {
                        null
                    } else {
                        viewHistoryInteractor.getViewItem(viewId = viewId)
                    }
                }
        ) { learnCourse, viewHistoryItem ->
            DataState(
                learnCourse = learnCourse!!,
                lastView = viewHistoryItem
            )
        }
            .onEach { dataState ->
                dataStateAsFlow.update {
                    dataState
                }
            }
            .catch { onGetError(it) }
            .launchIn(mainScope)
        dataStateAsFlow
            .onEach { dataState ->
                viewState = viewState.copy(
                    type = if (dataState == null) {
                        State.Type.Initialization
                    } else {
                        viewStateMapper.mapData(
                            learnCourse = dataState.learnCourse,
                            lastView = dataState.lastView
                        )
                    }
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

    private fun launchWithLoader(
        onError: (suspend (Throwable) -> Unit)? = ::onGetError,
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

    internal class DataState(
        val learnCourse: LearnCourseEntity,
        val lastView: ViewHistoryItem?
    )
}