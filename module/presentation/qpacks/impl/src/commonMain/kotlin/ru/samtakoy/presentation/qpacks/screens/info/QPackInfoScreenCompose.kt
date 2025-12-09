package ru.samtakoy.presentation.qpacks.screens.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButton
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogView
import ru.samtakoy.presentation.core.design_system.dropdown.MyDropDownMenuBox
import ru.samtakoy.presentation.core.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItem
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.presentation.core.design_system.toolbar.ToolbarTitleView
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.Event
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.NavigationAction
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.State
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.qpack_favorites_box

@Composable
internal fun QPackInfoScreen(
    viewModel: QPackInfoViewModel,
    onNavigationAction: (NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    val choiceDialogState: MutableState<MyChoiceDialogUiModel?> = remember { mutableStateOf(null) }

    QPackInfoScreenInternal(
        viewState = viewState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )

    HandleActions(
        viewModel = viewModel,
        onEvent = viewModel::onEvent,
        onNavigationAction = onNavigationAction,
        snackbarHostState = snackbarHostState,
        choiceDialogState = choiceDialogState,
    )

    ScreenDialogs(
        choiceDialogState = choiceDialogState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ScreenDialogs(
    choiceDialogState: MutableState<MyChoiceDialogUiModel?>,
    onEvent: (event: Event) -> Unit
) {
    MyChoiceDialogView(
        dialogState = choiceDialogState,
        onButtonClick = { dialogId, buttonId, selectedItemId ->
            selectedItemId?.let {
                onEvent(
                    Event.ChoiceDialoButtonClick(
                        dialogId = dialogId,
                        buttonId = buttonId,
                        itemId = selectedItemId
                    )
                )
            }
        }
    )
}

@Composable
private fun HandleActions(
    viewModel: QPackInfoViewModel,
    onEvent: (event: Event) -> Unit,
    onNavigationAction: (NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    choiceDialogState: MutableState<MyChoiceDialogUiModel?>,
) {
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            QPackInfoViewModel.Action.OpenCardsInBottomList -> Unit // TODO()
            is QPackInfoViewModel.Action.RequestNewCourseCreation -> Unit // TODO()
            is QPackInfoViewModel.Action.RequestsSelectCourseToAdd -> Unit // TODO()
            is QPackInfoViewModel.Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
            is QPackInfoViewModel.Action.ShowChoiceDialog -> {
                choiceDialogState.value = action.dialogModel
            }
            is NavigationAction -> onNavigationAction(action)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QPackInfoScreenInternal(
    viewState: State,
    onEvent: (Event) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    MySimpleScreenScaffold(
        isLoaderVisible = viewState.isLoading,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopAppBar(
                title = { ToolbarTitleView(title = viewState.title, subtitle = null) },
                actions = {
                    MyDropDownMenuBox(
                        menu = viewState.toolbarMenu,
                        onMenuItemClick = {
                            onEvent(Event.ToolbarMenuItemClick(menuItemId = it.id))
                        }
                    ) { onContentClick ->
                        IconButton(onClick = onContentClick) {
                            Icon(Icons.Default.MoreVert, contentDescription = null)
                        }
                    }
                }
            )
            Column(
                modifier = Modifier.padding(
                    horizontal = UiOffsets.screenContentHPadding,
                    vertical = UiOffsets.screenContentVPadding
                )
            ) {
                Text(
                    text = viewState.cardsCountText,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleSmall
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = UiOffsets.screenContentVPadding)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        UiOffsets.itemsStandartVOffset,
                        Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    viewState.buttons.forEach { buttonModel ->
                        key(buttonModel.id) {
                            MyButton(
                                model = buttonModel,
                                onClick = remember { { onEvent(Event.ButtonClick(it)) } }
                            )
                        }
                    }
                }
                MySelectableItem(
                    model = getIsFavoriteModel(viewState.isFavoriteChecked),
                    onClick = { onEvent(Event.FavoriteChange(it.isChecked)) },
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun getIsFavoriteModel(isFavoriteChecked: Boolean): MySelectableItemModel {
    val title = stringResource(Res.string.qpack_favorites_box).asA()
    return remember(isFavoriteChecked) {
        MySelectableItemModel(
            AnyUiId(),
            title,
            isChecked = isFavoriteChecked,
            isEnabled = true
        )
    }
}
