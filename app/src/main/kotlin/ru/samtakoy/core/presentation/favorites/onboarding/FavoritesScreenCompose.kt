package ru.samtakoy.core.presentation.favorites.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.core.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.core.presentation.design_system.base.UiOffsets
import ru.samtakoy.core.presentation.design_system.button.MyButton
import ru.samtakoy.core.presentation.design_system.button.MyButtonModel
import ru.samtakoy.core.presentation.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.core.presentation.favorites.onboarding.mapper.FavoritesButtonsMapper
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModel

@Composable
internal fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNavigationAction: (FavoritesViewModel.NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    FavoritesScreenInternal(
        viewState = viewState,
        onViewEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
    HandleActions(viewModel, onNavigationAction, snackbarHostState)
}

@Composable
private fun HandleActions(
    viewModel: FavoritesViewModel,
    onNavigationAction: (FavoritesViewModel.NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            is FavoritesViewModel.NavigationAction -> {
                onNavigationAction(action)
            }
            is FavoritesViewModel.Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
        }
    }
}

@Composable
private fun FavoritesScreenInternal(
    viewState: FavoritesViewModel.State,
    onViewEvent: (FavoritesViewModel.Event) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    MySimpleScreenScaffold(
        isLoaderVisible = viewState.isLoaderVisible,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UiOffsets.screenContentHOffset)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            viewState.buttons.forEach { buttonModel ->
                key(buttonModel.id) {
                    val updatedModel = rememberUpdatedState(buttonModel)
                    MyButton(
                        model = buttonModel,
                        onClick = {
                            onViewEvent(
                                FavoritesViewModel.Event.ButtonClick(id = updatedModel.value.id)
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun FavoritesScreenInternal_Preview() = MaterialTheme {
    FavoritesScreenInternal(
        viewState = FavoritesViewModel.State(
            isLoaderVisible = true,
            buttons = listOf(
                MyButtonModel(FavoritesButtonsMapper.ButtonId.AllFavoriteCards, "test".asAnnotated()),
                MyButtonModel(FavoritesButtonsMapper.ButtonId.AllFavoriteCards, "test2".asAnnotated())
            ).toImmutableList()
        ),
        onViewEvent = {},
        snackbarHostState = SnackbarHostState(),
        modifier = Modifier
            .fillMaxWidth()
    )
}