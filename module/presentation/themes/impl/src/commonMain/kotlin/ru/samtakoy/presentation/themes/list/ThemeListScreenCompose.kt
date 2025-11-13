package ru.samtakoy.presentation.themes.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.presentation.core.appelements.qpacklistitem.QPackListItemView
import ru.samtakoy.presentation.core.appelements.themelistitem.ThemeListItemView
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.dialogs.alert.MyAlertDialogUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.alert.MyAlertDialogView
import ru.samtakoy.presentation.core.design_system.dialogs.inputtext.MyInputTextDialogUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.inputtext.MyInputTextDialogView
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel
import ru.samtakoy.presentation.core.design_system.dropdown.MyDropDownMenuBox
import ru.samtakoy.presentation.core.design_system.progress.ProgressOverlayView
import ru.samtakoy.presentation.core.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.presentation.core.design_system.scrollbar.vertical.PlatformVerticalScrollbar
import ru.samtakoy.presentation.core.design_system.toolbar.ToolbarTitleView
import ru.samtakoy.presentation.themes.list.model.ThemeUiItem
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel.Event

@Composable
internal fun ThemeListScreen(
    viewModel: ThemeListViewModel,
    onMainNavigator: () -> Unit,
    onNavigationAction: (ThemeListViewModel.NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    var inputDialogState: MutableState<MyInputTextDialogUiModel?> =  remember {
        mutableStateOf(null)
    }
    var alertDialogState: MutableState<MyAlertDialogUiModel?> =  remember {
        mutableStateOf(null)
    }

    ThemesListScreenInternal(
        viewState = viewState,
        onMainNavigator = onMainNavigator,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )

    HandleActions(
        viewModel = viewModel,
        onEvent = viewModel::onEvent,
        onNavigationAction = onNavigationAction,
        snackbarHostState = snackbarHostState,
        inputDialogState = inputDialogState,
        alertDialogState = alertDialogState
    )

    ScreenDialogs(
        inputDialogState = inputDialogState,
        alertDialogState = alertDialogState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun ScreenDialogs(
    inputDialogState: MutableState<MyInputTextDialogUiModel?>,
    alertDialogState: MutableState<MyAlertDialogUiModel?>,
    onEvent: (event: Event) -> Unit
) {
    MyInputTextDialogView(
        dialogState = inputDialogState,
        onButtonClick = { dialogId, result ->
            onEvent(Event.InputDialogResult(dialogId, result))
        },
        onDismiss = null
    )
    MyAlertDialogView(
        dialogState = alertDialogState,
        onButtonClick = { dialogId, buttonId ->
            onEvent(Event.AlertDialogResult(dialogId, buttonId))
        },
        onDismiss = null
    )
}

@Composable
private fun HandleActions(
    viewModel: ThemeListViewModel,
    onEvent: (event: Event) -> Unit,
    onNavigationAction: (ThemeListViewModel.NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    inputDialogState: MutableState<MyInputTextDialogUiModel?>,
    alertDialogState: MutableState<MyAlertDialogUiModel?>
) {
    // TODO унести в репозиторий
    val pickZipFileLauncher = rememberFilePickerLauncher(
        type = FileKitType.File(
            // не дает выбраать zip
            // extensions = listOf("zip")
           )
    ) { file ->
        onEvent(Event.ImportFileSelected(file))
    }
    val pickAnyFileLauncher = rememberFilePickerLauncher(
        type = FileKitType.File(extensions = null)
    ) { file ->
        onEvent(Event.ImportFileSelected(file))
    }

    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            is ThemeListViewModel.NavigationAction -> onNavigationAction(action)
            is ThemeListViewModel.Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
            ThemeListViewModel.Action.ShowFolderSelectionDialog -> {
                snackbarHostState.showSnackbar("пока не реализовано")
            }
            is ThemeListViewModel.Action.ShowImportPackFileSelection -> {
                if (action.isZip) {
                    pickZipFileLauncher.launch()
                } else {
                    pickAnyFileLauncher.launch()
                }
            }
            is ThemeListViewModel.Action.ShowInputThemeTitleDialog -> {
                inputDialogState.value = action.dialogModel
            }
            is ThemeListViewModel.Action.ShowAlertDialog -> {
                alertDialogState.value = action.dialogModel
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ThemesListScreenInternal(
    viewState: ThemeListViewModel.State,
    onMainNavigator: () -> Unit,
    onEvent: (event: Event) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    MySimpleScreenScaffold(
        isLoaderVisible = viewState.isLoading,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        val scrollState = rememberLazyListState()
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = {
                    ToolbarTitleView(
                        title = viewState.toolbarTitle,
                        subtitle = viewState.toolbarSubtitle
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onMainNavigator()
                        }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(Event.AddNewThemeRequest) }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                    MyDropDownMenuBox(
                        menu = viewState.toolbarMenu,
                        onMenuItemClick = {
                            onEvent(Event.ToolbarMenuItemClick(id = it.id))
                        }
                    ) { onContentClick ->
                        IconButton(onClick = onContentClick) {
                            Icon(Icons.Default.MoreVert, contentDescription = null)
                        }
                    }
                }
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = UiOffsets.screenContentHPadding,
                        vertical = UiOffsets.screenContentVPadding
                    ),
                verticalArrangement = Arrangement.spacedBy(UiOffsets.listItemOffset),
                state = scrollState
            ) {
                items(
                    items = viewState.items,
                    key = { it.composeKey }
                ) { item ->
                    when (item) {
                        is ThemeUiItem.QPack -> QPackListItemInternalView(
                            menu = viewState.qPackContextMenu,
                            onEvent = onEvent,
                            item = item
                        )
                        is ThemeUiItem.Theme -> ThemeListItemInternalView(
                            menu = viewState.themeContextMenu,
                            onEvent = onEvent,
                            item = item
                        )
                    }
                }
            }
        }

        // TODO Временно для десктопа, пока не подумаю
        if (scrollState.canScrollForward || scrollState.canScrollBackward) {
            PlatformVerticalScrollbar(
                scrollState = scrollState,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
            )
        }
    }
    viewState.progressPanel?.let {
        ProgressOverlayView(model = viewState.progressPanel)
    }
}

@Composable
private fun QPackListItemInternalView(
    menu: DropDownMenuUiModel,
    onEvent: (Event) -> Unit,
    item: ThemeUiItem.QPack
) {
    val updatedItem: ThemeUiItem.QPack by rememberUpdatedState(item)
    MyDropDownMenuBox(
        menu = menu,
        onMenuItemClick = { menuItem ->
            onEvent(
                Event.QPackContextMenuItemClick(
                    item = updatedItem,
                    menuItem = menuItem
                )
            )
        }
    ) { longClickCallback ->
        QPackListItemView(
            id = updatedItem.id,
            title = updatedItem.title,
            creationDate = updatedItem.creationDate,
            modifier = Modifier,
            viewCount = updatedItem.viewCount,
            onClick = { onEvent(Event.ListItemClick(updatedItem)) },
            onLongClick = { longClickCallback() }
        )
    }
}

@Composable
private fun ThemeListItemInternalView(
    menu: DropDownMenuUiModel,
    onEvent: (Event) -> Unit,
    item: ThemeUiItem.Theme
) {
    val updatedItem: ThemeUiItem.Theme by rememberUpdatedState(item)
    MyDropDownMenuBox(
        menu = menu,
        onMenuItemClick = { menuItem ->
            onEvent(
                Event.ThemeContextMenuItemClick(
                    item = updatedItem,
                    menuItem = menuItem
                )
            )
        }
    ) { longClickCallback ->
        ThemeListItemView(
            id = updatedItem.id,
            title = updatedItem.title,
            modifier = Modifier,
            onClick = { onEvent(Event.ListItemClick(updatedItem)) },
            onLongClick = { longClickCallback() }
        )
    }
}