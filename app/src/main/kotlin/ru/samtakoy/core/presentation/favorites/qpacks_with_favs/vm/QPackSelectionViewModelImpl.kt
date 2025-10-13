package ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.domain.FavoritesInteractor
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.core.presentation.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.mapper.QPacksWithFavsItemsMapper
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel.Action
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel.Event
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel.NavigationAction
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel.State
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.view.ViewHistoryInteractor

class QPackSelectionViewModelImpl(
    private val qPackInteractor: QPackInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val itemsMapper: QPacksWithFavsItemsMapper,
    private val resources: Resources,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        isLoaderVisible = true,
        items = emptyList<MySelectableItemModel>().toImmutableList(),
        actionButton = itemsMapper.mapActionButton()
    )
), QPackSelectionViewModel {

    private val selectedItemsFlow = MutableStateFlow<Map<Long, Boolean>>(mutableMapOf())

    init {
        subscribePacks()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.ItemClick -> onItemClick(event.item)
            Event.ActionButtonClick -> onActionButtonClick()
        }
    }

    private fun onActionButtonClick() {
        viewState = viewState.copy(isLoaderVisible = true)
        launchCatching(
            onError = ::onSomeError,
            onFinally = {
                viewState = viewState.copy(isLoaderVisible = false)
            }
        ) {
            val qPackIds = selectedItemsFlow.value.map { it.key }
            if (qPackIds.isEmpty()) {
                sendAction(
                    Action.ShowErrorMessage(resources.getString(R.string.qpacks_with_favs_no_selected_packs))
                )
                return@launchCatching
            }
            val newView = viewHistoryInteractor.addNewViewItem(
                qPackId = 0,
                cardIds = favoritesInteractor.getAllFavoriteCardsIdsFromQPacks(qPackIds)
            )
            sendAction(NavigationAction.ViewCardsFromCourse(viewItemId = newView.id))
        }
    }

    private fun onItemClick(item: MySelectableItemModel) {
        val id = item.id as? LongUiId ?: return
        selectedItemsFlow.update {  selectedMap ->
            selectedMap.toMutableMap().apply {
                this[id.value] = !item.isChecked
            }
        }
    }

    private fun subscribePacks() {
        combine(
            getQPacksFlow(),
            selectedItemsFlow
        ) { qPacks, selectedMap ->
            viewState = viewState.copy(
                items = qPacks.map { qPack ->
                    itemsMapper.map(
                        item = qPack,
                        isSelected = selectedMap[qPack.id] == true
                    )
                }.toImmutableList()
            )
        }.launchIn(mainScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getQPacksFlow(): Flow<List<QPack>> {
        return favoritesInteractor.getAllQPacksIdsByCreationDateDescWithFavsAsFlow()
            .mapLatest {
                qPackInteractor.getQPacksByIds(it)
            }
    }

    private fun onSomeError(t: Throwable) {
        sendAction(Action.ShowErrorMessage(resources.getString(R.string.common_err_message)))
    }
}