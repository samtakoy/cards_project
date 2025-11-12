package ru.samtakoy.presentation.cards.screens.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.Action
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.CardState
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.Event
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.NavigationAction
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.State
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.round.MyFabButtonIcon
import ru.samtakoy.presentation.core.design_system.button.round.MyFabButtonUiModel
import ru.samtakoy.presentation.core.design_system.button.round.MyFabButtonView
import ru.samtakoy.presentation.core.design_system.button.usual.MyButton
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItem
import ru.samtakoy.presentation.core.design_system.toolbar.ToolbarTitleView
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.cards_view_favorite_box

@Composable
internal fun CardsViewScreen(
    viewModel: CardsViewViewModel,
    onNavigationAction: (NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()

    CardsViewScreenInternal(
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
    viewModel: CardsViewViewModel,
    onNavigationAction: (NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            is NavigationAction -> onNavigationAction(action)
            is Action.ShowEditTextDialog -> TODO()
            is Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
        }
    }
}

@Composable
internal fun CardsViewScreenInternal(
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
        when (val type = viewState.type) {
            is State.Type.Card -> CardState(
                state = type,
                cardItems = viewState.cardItems,
                questionButtons = viewState.questionButtons,
                answerButtons = viewState.answerButtons,
                onEvent = onEvent
            )
            is State.Type.Error -> ErrorState(state = type)
            State.Type.Initialization -> InitState()
        }
    }
}

@Composable
private fun BoxScope.InitState(
    modifier: Modifier = Modifier
) {
    // ?
}

@Composable
private fun BoxScope.ErrorState(
    state: State.Type.Error,
    modifier: Modifier = Modifier
) {
    Text(
        text = state.errorText,
        modifier = modifier.align(alignment = Alignment.Center),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyMedium
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardState(
    state: State.Type.Card,
    cardItems: ImmutableList<CardState>,
    questionButtons: ImmutableList<MyButtonUiModel>,
    answerButtons: ImmutableList<MyButtonUiModel>,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                ToolbarTitleView(title = state.cardsCountTitle, subtitle = null)
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val pagerState = rememberPagerState(
                pageCount = { cardItems.size }
            )
            LaunchedEffect(state.cardIndex) {
                if (state.cardIndex != pagerState.currentPage) {
                    pagerState.animateScrollToPage(state.cardIndex)
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { pageIndex ->
                val cardState = cardItems.getOrNull(pageIndex)
                if (cardState != null) {
                    val content = cardState?.content

                    when {
                        content == null -> CardNotInitView()
                        !cardState.isQuestion -> CardAnswerView(
                            cardState = cardState,
                            buttons = answerButtons,
                            cardContent = content,
                            onEvent = onEvent,
                            modifier = Modifier.padding(horizontal = UiOffsets.screenContentHPadding)
                        )
                        cardState.isQuestion -> CardQuestionView(
                            cardState = cardState,
                            buttons = questionButtons,
                            cardContent = content,
                            onEvent = onEvent,
                            modifier = Modifier.padding(horizontal = UiOffsets.screenContentHPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardNotInitView(
    modifier: Modifier = Modifier
) {
    // do nothing?
}

@Composable
private fun CardQuestionView(
    cardState: CardState,
    buttons: ImmutableList<MyButtonUiModel>,
    cardContent: CardState.Content,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        FavoriteCheckBox(
            isChecked = cardState.content?.isFavorite == true,
            onEvent = onEvent,
            modifier = Modifier.align(Alignment.End)
        )
        TextContainer(
            text = cardContent.text,
            hasRevertButton = cardContent.hasRevertButton,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        ButtonsRow(
            buttons = buttons,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CardAnswerView(
    cardState: CardState,
    buttons: ImmutableList<MyButtonUiModel>,
    cardContent: CardState.Content,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        FavoriteCheckBox(
            isChecked = cardState.content?.isFavorite == true,
            onEvent = onEvent,
            modifier = Modifier.align(Alignment.End)
        )
        TextContainer(
            text = cardContent.text,
            hasRevertButton = cardContent.hasRevertButton,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        ButtonsRow(
            buttons = buttons,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ColumnScope.FavoriteCheckBox(
    isChecked: Boolean,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    MySelectableItem(
        text = stringResource(Res.string.cards_view_favorite_box).asAnnotated(),
        isChecked = isChecked,
        isEnabled = true,
        onClick = {
            onEvent(Event.FavoriteClick)
        },
        modifier = modifier,
        contentDescription = stringResource(Res.string.cards_view_favorite_box)
    )
}

@Composable
private fun ColumnScope.TextContainer(
    text: AnnotatedString,
    hasRevertButton: Boolean,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        val scrollState = rememberScrollState()
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .verticalScroll(scrollState),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
        if (hasRevertButton) {
            MyFabButtonView(
                model = remember {
                    MyFabButtonUiModel(id = AnyUiId(), icon = MyFabButtonIcon.Revert)
                },
                onClick = { onEvent(Event.RevertClick) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(MyOffsets.large)
            )
        }
    }
}

@Composable
private fun ButtonsRow(
    buttons: ImmutableList<MyButtonUiModel>,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MyOffsets.medium, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        buttons.forEach {
            key(it.id) {
                MyButton(
                    model = it,
                    onClick = {
                        onEvent(Event.ButtonClick(it.id))
                    }
                )
            }
        }
    }
}