package ru.samtakoy.core.presentation.favorites.qpacks_with_favs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.core.presentation.design_system.base.UiOffsets
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.core.presentation.design_system.button.MyButton
import ru.samtakoy.core.presentation.design_system.button.MyButtonModel
import ru.samtakoy.core.presentation.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.core.presentation.design_system.selectable_item.MySelectableItem
import ru.samtakoy.core.presentation.design_system.selectable_item.getPreviewSelectableItems
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel.Event

@Composable
fun QPackSelectionScreen(
    viewModel: QPackSelectionViewModel,
    onNavigationAction: (QPackSelectionViewModel.NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    QPackSelectionScreenInternal(
        viewState = viewState,
        onViewEvent = viewModel::onEvent,
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
    viewModel: QPackSelectionViewModel,
    onNavigationAction: (QPackSelectionViewModel.NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            is QPackSelectionViewModel.NavigationAction -> onNavigationAction(action)
            is QPackSelectionViewModel.Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
        }
    }
}

@Composable
private fun QPackSelectionScreenInternal(
    viewState: QPackSelectionViewModel.State,
    onViewEvent: (Event) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    MySimpleScreenScaffold(
        isLoaderVisible = viewState.isLoaderVisible,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.matchParentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = UiOffsets.screenContentHOffset)
            ) {
                items(
                    items = viewState.items,
                    key = { it.id }
                ) { itemModel ->
                    MySelectableItem(
                        model = itemModel,
                        onClick = {
                            onViewEvent(Event.ItemClick(itemModel))
                        }
                    )
                }
            }
            MyButton(
                model = viewState.actionButton,
                onClick = {
                    onViewEvent(Event.ActionButtonClick)
                },
            )
        }
    }
}

@Preview
@Composable
private fun QPackSelectionScreenInternal_Preview() = MaterialTheme {
    QPackSelectionScreenInternal(
        viewState = QPackSelectionViewModel.State(
            isLoaderVisible = false,
            items = getPreviewSelectableItems(),
            actionButton = MyButtonModel(LongUiId(1L), "test".asAnnotated())
        ),
        onViewEvent = {},
        snackbarHostState = SnackbarHostState(),
        modifier = Modifier.fillMaxWidth()
    )
}