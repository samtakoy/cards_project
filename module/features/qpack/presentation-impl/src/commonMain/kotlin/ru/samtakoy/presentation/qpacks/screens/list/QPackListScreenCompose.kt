package ru.samtakoy.presentation.qpacks.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.presentation.core.appelements.qpacklistitem.QPackListItemView
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.usual.MyButton
import ru.samtakoy.presentation.core.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.presentation.core.design_system.scrollbar.vertical.PlatformVerticalScrollbar
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItem
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.presentation.core.design_system.toolbar.ToolbarTitleView
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModel
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModel.Event
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModel.NavigationAction
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.qpacks_favorites_check_box_title
import ru.samtakoy.resources.qpacks_search_hint

@Composable
internal fun QPackListScreen(
    viewModel: QPackListViewModel,
    onNavigationAction: (NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()

    QPackListScreenInternal(
        viewState = viewState,
        searchText = viewModel.searchText,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )

    HandleActions(
        viewModel = viewModel,
        onNavigationAction = onNavigationAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun HandleActions(
    viewModel: QPackListViewModel,
    onNavigationAction: (NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            is NavigationAction -> {
                onNavigationAction(action)
            }
            is QPackListViewModel.Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
        }
    }
}

@Composable
internal fun QPackListScreenInternal(
    viewState: QPackListViewModel.State,
    searchText: MutableState<String>,
    onEvent: (event: Event) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val itemsListState = rememberLazyListState()
    /* Это альтернатива LaunchedEffect, но тогда без анимаций:
    rememberSaveable(viewState.scrollPoint.id, saver = LazyListState.Saver) {
        // Пересоздаем состояние прокрутки при изменении параметров от viewModel
        // (при фильтрации и сортировках мы не хотим дефолтное поведение сохранения позиции первого видимого элемента в списке)
        LazyListState(
            viewState.scrollPoint.scrollIndexBeforeOp,
            viewState.scrollPoint.scrollOffsetBeforeOp
        )
    }*/
    LaunchedEffect(viewState.scrollPoint.id) {
        itemsListState.scrollToItem(
            viewState.scrollPoint.scrollIndexBeforeOp,
            viewState.scrollPoint.scrollOffsetBeforeOp
        )
    }

    MySimpleScreenScaffold(
        isLoaderVisible = viewState.isLoading,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { ToolbarTitleView(title = viewState.title, subtitle = null) },
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(Event.MainNavigatorIconClick) }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                },
            )
            Column(
                modifier = Modifier.padding(
                    start = MyTheme.offsets.screenContentHPadding,
                    end = MyTheme.offsets.screenContentHPadding,
                    bottom = MyTheme.offsets.screenContentVPadding
                ),
                verticalArrangement = Arrangement.spacedBy(MyTheme.offsets.itemsSmallVOffset)
            ) {
                OutlinedTextField(
                    value = searchText.value,
                    onValueChange = { newText ->
                        searchText.value = newText
                        onEvent(Event.SearchTextChange(searchText.value))
                    },
                    label = { Text(stringResource(Res.string.qpacks_search_hint)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                MySelectableItem(
                    model = getIsFavoriteModel(viewState.isFavoritesChecked),
                    onClick = { onEvent(Event.FavoritesCheckBoxChange(it.isChecked)) },
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.End)
                )
                if (viewState.sortButton != null) {
                    val updatedItemsListState by rememberUpdatedState(itemsListState)
                    MyButton(
                        model = viewState.sortButton,
                        onClick = remember {
                            {
                                onEvent(
                                    Event.SortButtonClick(it, updatedItemsListState.firstVisibleItemIndex, updatedItemsListState.firstVisibleItemScrollOffset)
                                )
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier,
                        state = itemsListState,
                        verticalArrangement = Arrangement.spacedBy(MyTheme.offsets.listItemOffset)
                    ) {
                        items(
                            items = viewState.items,
                            key = { it.id.value }
                        ) { item ->
                            QPackListItemView(
                                model = item,
                                modifier = Modifier.animateItem(),
                                onClick = { onEvent(Event.PackClick(it)) }
                            )
                        }
                    }
                    if (itemsListState.canScrollForward || itemsListState.canScrollBackward) {
                        PlatformVerticalScrollbar(
                            scrollState = itemsListState,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getIsFavoriteModel(isFavoriteChecked: Boolean): MySelectableItemModel {
    val title = stringResource(Res.string.qpacks_favorites_check_box_title).asA()
    return remember(isFavoriteChecked) {
        MySelectableItemModel(
            id = AnyUiId(),
            text = title,
            isChecked = isFavoriteChecked,
            isEnabled = true
        )
    }
}