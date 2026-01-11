package ru.samtakoy.presentation.qpacks.screens.list.vm

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
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.common.utils.log.MyLog
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.qpack.QPackSortUseCase
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.presentation.base.viewmodel.savedstate.toSavedState
import ru.samtakoy.presentation.core.appelements.qpacklistitem.QPackListItemUiModel
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.qpacks.screens.list.mapper.QPackListItemUiModelMapper
import ru.samtakoy.presentation.qpacks.screens.list.mapper.QPackListSortButtonMapper
import ru.samtakoy.presentation.qpacks.screens.list.model.QPackListSortButtonId
import ru.samtakoy.presentation.qpacks.screens.list.model.QPackSortType
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModel.NavigationAction
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.qpacks_request_error

internal class QPackListViewModelImpl(
    private val qPackSortUseCase: QPackSortUseCase,
    private val itemsMapper: QPackListItemUiModelMapper,
    private val sortButtonMapper: QPackListSortButtonMapper,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider
) : BaseViewModelImpl<QPackListViewModel.State, QPackListViewModel.Action, QPackListViewModel.Event>(
    scopeProvider = scopeProvider,
    initialState = QPackListViewModel.State(
        isLoading = true,
        title = "".asAnnotated(),
        items = emptyList<QPackListItemUiModel>().toImmutableList(),
        scrollPoint = QPackListViewModel.LastScrollPoint(0, 0, 0),
        isFavoritesChecked = false,
        sortButton = null
    )
), QPackListViewModel {

    private val isFavoriteState = SavedStateValue<Boolean>(
        initialValueGetter = { viewState.isFavoritesChecked },
        keyName = KEY_IS_FAVORITE,
        savedStateHandle = savedStateHandle,
        serialize = { it.toString() },
        deserialize = { it.toBoolean() },
        saveScope = ioScope
    )

    private val sortTypeState = SavedStateValue<QPackSortType>(
        initialValueGetter = { QPackSortType.CREATION_DATE_DESC },
        keyName = KEY_SORT_TYPE,
        savedStateHandle = savedStateHandle,
        serialize = { Json.encodeToString(it) },
        deserialize = { Json.decodeFromString(it) },
        saveScope = ioScope
    )

    override val searchText = mutableStateOf("").toSavedState(
        keyName = KEY_SEARCH_TEXT,
        savedStateHandle = savedStateHandle,
        serialize = { it },
        deserialize = { it as String },
        saveScope = ioScope
    )

    var lastScrollPoint = QPackListViewModel.LastScrollPoint(0, 0, 0)

    init {
        subscribeData()
        launchCatching {
            updateState { state ->
                state.copy(
                    sortButton = sortButtonMapper.mapSortButton(sortTypeState.value)
                )
            }
        }
    }

    override fun onEvent(event: QPackListViewModel.Event) {
        when (event) {
            is QPackListViewModel.Event.FavoritesCheckBoxChange -> onUiFavoritesCheckBoxChange(event.isChecked)
            is QPackListViewModel.Event.PackClick -> onUiPackClick(event.itemId)
            is QPackListViewModel.Event.SearchTextChange -> onUiSearchTextChange(event.text)
            is QPackListViewModel.Event.SortButtonClick -> onUiSortButtonClick(event.buttonId, event.scrollIndex, event.scrollOffset)
            QPackListViewModel.Event.MainNavigatorIconClick -> sendAction(NavigationAction.OpenMainNavigator)
        }
    }

    private fun onUiSearchTextChange(text: String) {
        updateLastScrollPoint(0, 0)
        searchText.value = text
    }

    private fun onUiFavoritesCheckBoxChange(isChecked: Boolean) {
        updateLastScrollPoint(0, 0)
        isFavoriteState.value = isChecked.not()
    }

    private fun onUiPackClick(packId: LongUiId) {
        sendAction(NavigationAction.ShowPackInfo(packId.value))
    }

    private fun onUiSortButtonClick(id: UiId, scrollIndex: Int, scrollOffset: Int) {
        updateLastScrollPoint(scrollIndex, scrollOffset)
        val buttonId = id as? QPackListSortButtonId ?: return
        when(buttonId) {
            QPackListSortButtonId.LastViewDateAsc -> {
                sortTypeState.value = QPackSortType.CREATION_DATE_DESC
            }
            QPackListSortButtonId.CreationDateDesc -> {
                sortTypeState.value = QPackSortType.LAST_VIEW_DATE_ASC
            }
        }
    }

    private fun updateLastScrollPoint(scrollIndex: Int, scrollOffset: Int) {
        lastScrollPoint = lastScrollPoint.copy(
            id = lastScrollPoint.id + 1,
            scrollIndexBeforeOp = scrollIndex,
            scrollOffsetBeforeOp = scrollOffset
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun subscribeData() {
        isFavoriteState.asFlow()
            .onEach { isFavorite ->
                updateState { it.copy(isFavoritesChecked = isFavorite) }
            }
            .launchIn(mainScope)
        sortTypeState.asFlow()
            .onEach { sortType ->
                updateState {
                    it.copy(sortButton = sortButtonMapper.mapSortButton(sortType))
                }
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
        }.distinctUntilChanged()
        .flatMapLatest {
            getAllQPacksSortedWithSortTypeAsFlow(
                sortType = it.second,
                isFavorites = it.first,
                searchString = it.third
            )
        }.onEach { (sortType: QPackSortType, list: List<QPack>, searchString: String) ->
            updateState {
                it.copy(
                    items = itemsMapper.map(list, sortType).toImmutableList(),
                    scrollPoint = lastScrollPoint
                )
            }
        }.catch {
            updateState { it.copy(isLoading = false) }
            onGetError(it)
        }.onStart {
            updateState { it.copy(isLoading = false) }
        }
        .launchIn(mainScope)
    }

    private fun getAllQPacksSortedWithSortTypeAsFlow(
        sortType: QPackSortType,
        isFavorites: Boolean,
        searchString: String
    ): Flow<Triple<QPackSortType, List<QPack>, String>> {
        return getAllQPacksSortedAsFlow(sortType, isFavorites, searchString)
            .map { list ->
                Triple(
                    sortType,
                    list,
                    searchString
                )
            }
    }

    private fun getAllQPacksSortedAsFlow(
        sortType: QPackSortType,
        isFavorites: Boolean,
        searchString: String
    ): Flow<List<QPack>> {
        return when (sortType) {
            QPackSortType.LAST_VIEW_DATE_ASC -> qPackSortUseCase.getAllQPacksByLastViewDateAscAsFlow(
                searchString,
                isFavorites
            )
            QPackSortType.CREATION_DATE_DESC -> qPackSortUseCase.getAllQPacksByCreationDateDescAsFlow(
                searchString,
                isFavorites
            )
        }
    }

    private suspend fun onGetError(t: Throwable) {
        MyLog.add(t.stackTraceToString())
        sendAction(
            QPackListViewModel.Action.ShowErrorMessage(
                getString(Res.string.qpacks_request_error)
            )
        )
    }

    companion object {
        private const val SEARCH_DELAY: Long = 1000
        private const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
        private const val KEY_IS_FAVORITE = "KEY_IS_FAVORITE"
        private const val KEY_SORT_TYPE = "KEY_SORT_TYPE"
    }
}