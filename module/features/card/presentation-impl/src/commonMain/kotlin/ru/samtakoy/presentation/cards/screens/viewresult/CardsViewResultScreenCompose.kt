package ru.samtakoy.presentation.cards.screens.viewresult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.Event
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.NavigationAction
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.State
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.button.usual.MyButton
import ru.samtakoy.presentation.core.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.presentation.core.design_system.toolbar.ToolbarTitleView

@Composable
internal fun CardsViewResultScreen(
    viewModel: CardsViewResultViewModel,
    onNavigationAction: (NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()

    CardsViewResultScreenInternal(
        viewState = viewState,
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
    viewModel: CardsViewResultViewModel,
    onNavigationAction: (NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            is CardsViewResultViewModel.Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
            is CardsViewResultViewModel.Action.ShowScheduleEditDialog -> {
                // TODO
            }
            is NavigationAction -> onNavigationAction(action)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CardsViewResultScreenInternal(
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
        val content = viewState.content
        if (content != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopAppBar(
                    title = { ToolbarTitleView(title = content.title, subtitle = null) }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = UiOffsets.screenContentHPadding,
                            vertical = UiOffsets.screenContentVPadding
                        )
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        UiOffsets.itemsStandartVOffset,
                        Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = content.viewedTitle,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.titleMedium
                    )
                    content.errorsTitle?.let {
                        Text(
                            text = content.errorsTitle,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = MyColors.getErrorTextColor(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    content.scheduleTitle?.let {
                        Spacer(modifier = Modifier.size(UiOffsets.itemsStandartVOffset))
                        Text(
                            text = content.scheduleTitle,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = MyColors.getSecondTextColor(),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    content.editScheduleButton?.let {
                        MyButton(
                            model = content.editScheduleButton,
                            onClick = {
                                onEvent(Event.ScheduleBtnClick)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.size(UiOffsets.itemsStandartVOffset))
                    MyButton(
                        model = content.okButton,
                        onClick = {
                            onEvent(Event.OkBtnClick)
                        }
                    )
                }
            }
            // end of content
        }
    }
}