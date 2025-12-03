package ru.samtakoy.oldlegacy.features.views.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.oldlegacy.features.views.presentation.history.components.OneViewItemView
import ru.samtakoy.oldlegacy.features.views.presentation.history.components.getOneViewItemViewPreviewItems
import ru.samtakoy.oldlegacy.features.views.presentation.history.vm.ViewsHistoryViewModel
import ru.samtakoy.oldlegacy.features.views.presentation.history.vm.ViewsHistoryViewModel.State
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme

@Composable
internal fun ViewsHistoryScreen(
    viewModel: ViewsHistoryViewModel,
    onNavigation: (ViewsHistoryViewModel.NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    ViewsHistoryScreenInternal(
        viewState = viewState,
        onViewEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
    HandleActions(
        viewModel = viewModel,
        onNavigation = onNavigation,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun HandleActions(
    viewModel: ViewsHistoryViewModel,
    onNavigation: (ViewsHistoryViewModel.NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            is ViewsHistoryViewModel.NavigationAction -> onNavigation(action)
            is ViewsHistoryViewModel.Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
        }
    }
}

@Composable
private fun ViewsHistoryScreenInternal(
    viewState: ViewsHistoryViewModel.State,
    onViewEvent: (ViewsHistoryViewModel.Event) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    MySimpleScreenScaffold(
        isLoaderVisible = viewState.type == ViewsHistoryViewModel.State.Type.Loading,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = UiOffsets.listItemOffset)
        ) {
            items(items = viewState.items, key = { it.id }) {
                OneViewItemView(
                    model = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = { onViewEvent(ViewsHistoryViewModel.Event.ItemClick(it)) }
                        )
                )
            }
        }
    }
}

@Preview
@Composable
private fun FavoritesScreenInternal_Preview() = MyTheme {
    ViewsHistoryScreenInternal(
        viewState = State(
            type = State.Type.Data,
            items = getOneViewItemViewPreviewItems().toImmutableList()
        ),
        onViewEvent = {},
        snackbarHostState = SnackbarHostState(),
        modifier = Modifier
            .fillMaxWidth()
    )
}