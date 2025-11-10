package ru.samtakoy.core.presentation.qpack.list.vm

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.presentation.base.viewmodel.savedstate.toSavedState
import ru.samtakoy.common.utils.log.MyLog
import ru.samtakoy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapper
import ru.samtakoy.core.presentation.qpack.list.model.QPackListItemUiModel
import ru.samtakoy.core.presentation.qpack.list.model.QPackSortType
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.Action
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.Event
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.NavAction.*
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.State
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.qpack.QPackInteractor

internal class QPacksListViewModelImpl(
    private val qPackInteractor: QPackInteractor,
    private val itemsMapper: QPackListItemUiModelMapper,
    private val resources: Resources,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        items = emptyList<QPackListItemUiModel>().toImmutableList(),
        isFavoritesChecked = false,
        sortType = QPackSortType.LAST_VIEW_DATE_ASC
    )
), QPacksListViewModel {

    private val isFavoriteState = SavedStateValue<Boolean>(
        initialValueGetter = { viewState.isFavoritesChecked },
        keyName = KEY_IS_FAVORITE,
        savedStateHandle = savedStateHandle,
        serialize = { it },
        deserialize = { it as Boolean },
        saveScope = ioScope
    )

    private val sortTypeState = SavedStateValue<QPackSortType>(
        initialValueGetter = { viewState.sortType },
        keyName = KEY_SORT_TYPE,
        savedStateHandle = savedStateHandle,
        serialize = { it },
        deserialize = { it as QPackSortType },
        saveScope = ioScope
    )

    val searchText = mutableStateOf("").toSavedState(
        keyName = KEY_SEARCH_TEXT,
        savedStateHandle = savedStateHandle,
        serialize = { it },
        deserialize = { it as String },
        saveScope = ioScope
    )

    init {
        subscribeData()
        launchCatching {
            sendAction(Action.SearchText(searchText.value))
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.FavoritesCheckBoxChange -> onUiFavoritesCheckBoxChange(event.isChecked)
            is Event.PackClick -> onUiPackClick(event.item)
            is Event.SearchTextChange -> onUiSearchTextChange(event.text)
            Event.SortByCreationDate -> onUiSortByCreationDate()
            Event.SortByLastViewDate -> onUiSortByLastViewDate()
        }
    }

    private fun onUiSearchTextChange(text: String) {
        searchText.value = text
    }

    private fun onUiFavoritesCheckBoxChange(isChecked: Boolean) {
        isFavoriteState.value = isChecked
    }

    private fun onUiPackClick(qPack: QPackListItemUiModel) {
        sendAction(ShowPackInfo(qPack.id.value))
    }

    private fun onUiSortByCreationDate() {
        sortTypeState.value = QPackSortType.CREATION_DATE_DESC
    }

    private fun onUiSortByLastViewDate() {
        sortTypeState.value = QPackSortType.LAST_VIEW_DATE_ASC
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun subscribeData() {
        isFavoriteState.asFlow()
            .onEach {
                viewState = viewState.copy(isFavoritesChecked = it)
            }
            .launchIn(mainScope)
        sortTypeState.asFlow()
            .onEach {
                viewState = viewState.copy(sortType = it)
            }
            .launchIn(mainScope)
        combine(
            isFavoriteState.asFlow(),
            sortTypeState.asFlow(),
            snapshotFlow { searchText.value }
                .distinctUntilChanged()
                .debounce(SEARCH_DELAY)
        ) { isFavorite, sortType, searchText ->
            Triple(isFavorite, sortType, searchText)
        }.flatMapLatest {
            getAllQPacksSortedWithSortTypeAsFlow(
                sortType = it.second,
                isFavorites = it.first,
                searchString = it.third
            )
        }.onEach { (sortType: QPackSortType, list: List<QPack>) ->
            if (sortTypeState.value == sortType) {
                viewState = viewState.copy(
                    items = itemsMapper.mapImmutableList(list, sortType)
                )
            }
        }.catch { onGetError(it) }
        .launchIn(mainScope)
    }

    private fun getAllQPacksSortedWithSortTypeAsFlow(
        sortType: QPackSortType,
        isFavorites: Boolean,
        searchString: String
    ): Flow<Pair<QPackSortType, List<QPack>>> {
        return getAllQPacksSortedAsFlow(sortType, isFavorites, searchString)
            .map { list ->
                Pair<QPackSortType, List<QPack>>(
                    sortType,
                    list
                )
            }
    }

    private fun getAllQPacksSortedAsFlow(
        sortType: QPackSortType,
        isFavorites: Boolean,
        searchString: String
    ): Flow<List<QPack>> {
        return when (sortType) {
            QPackSortType.LAST_VIEW_DATE_ASC -> qPackInteractor.getAllQPacksByLastViewDateAscAsFlow(
                searchString,
                isFavorites
            )
            QPackSortType.CREATION_DATE_DESC -> qPackInteractor.getAllQPacksByCreationDateDescAsFlow(
                searchString,
                isFavorites
            )
        }
    }

    private fun onGetError(t: Throwable) {
        MyLog.add(ExceptionUtils.getMessage(t))
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.qpacks_request_error))
        )
    }

    companion object {
        private const val SEARCH_DELAY: Long = 1000
        private const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
        private const val KEY_IS_FAVORITE = "KEY_IS_FAVORITE"
        private const val KEY_SORT_TYPE = "KEY_SORT_TYPE"
    }
}