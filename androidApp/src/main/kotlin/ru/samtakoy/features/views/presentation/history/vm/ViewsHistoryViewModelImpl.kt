package ru.samtakoy.features.views.presentation.history.vm

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.samtakoy.R
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.features.views.presentation.history.components.OneViewHistoryItemModel
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapper
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel.Action
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel.Event
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel.State

internal class ViewsHistoryViewModelImpl(
    private val viewsHistoryInteractor: ViewHistoryInteractor,
    private val itemsMapper: ViewHistoryItemUiModelMapper,
    private val resources: Resources,
    scopeProvider: ScopeProvider
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        type = State.Type.Loading,
        items = emptyList<OneViewHistoryItemModel>().toImmutableList()
    )
), ViewsHistoryViewModel {

    init {
        subscribeData()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.ItemClick -> onOneViewClick(event.item)
        }
    }

    private fun subscribeData() {
        viewsHistoryInteractor
            .getViewHistoryItemsWithThemeAsFlow()
            .distinctUntilChanged()
            .onEach { list ->
                viewState = viewState.copy(
                    type = State.Type.Data,
                    items = list.map(itemsMapper::map).toImmutableList()
                )
            }
            .catch {
                onSomeError(it)
            }
            .launchIn(
                mainScope
            )
    }

    private fun onOneViewClick(item: OneViewHistoryItemModel) {
        sendAction(
            ViewsHistoryViewModel.NavigationAction.NavigateToQPackInfo(
                qPackId = item.qPackId
            )
        )
    }

    private fun onSomeError(t: Throwable) {
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.common_err_message))
        )
    }
}