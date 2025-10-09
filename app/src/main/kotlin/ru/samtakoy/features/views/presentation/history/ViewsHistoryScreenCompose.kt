package ru.samtakoy.features.views.presentation.history

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.core.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.core.presentation.design_system.base.UiOffsets
import ru.samtakoy.core.presentation.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.features.views.presentation.history.components.OneViewItemView
import ru.samtakoy.features.views.presentation.history.components.getOneViewItemViewPreviewItems
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel.Action
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel.NavigationAction
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel.State

@Composable
internal fun ViewsHistoryScreen(
    viewModel: ViewsHistoryViewModel,
    onNavigation: (NavigationAction) -> Unit,
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
    onNavigation: (NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            is NavigationAction -> onNavigation(action)
            is Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
        }
    }
}

@Composable
private fun ViewsHistoryScreenInternal(
    viewState: State,
    onViewEvent: (ViewsHistoryViewModel.Event) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    MySimpleScreenScaffold(
        isLoaderVisible = viewState.type == State.Type.Loading,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = UiOffsets.screenContentHOffset)
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
private fun FavoritesScreenInternal_Preview() = MaterialTheme {
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