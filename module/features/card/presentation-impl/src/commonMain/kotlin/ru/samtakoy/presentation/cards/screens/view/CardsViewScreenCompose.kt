package ru.samtakoy.presentation.cards.screens.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxLanguage
import generateAnnotatedString
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.presentation.cards.screens.view.model.CodeType
import ru.samtakoy.presentation.cards.screens.view.model.ContentPart
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.Action
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.CardState
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.Event
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.NavigationAction
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.State
import ru.samtakoy.presentation.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.base.utils.getRoundedShape
import ru.samtakoy.presentation.core.design_system.base.utils.getTopRoundedShape
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonIcon
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonSize
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonUiModel
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonView
import ru.samtakoy.presentation.core.design_system.button.usual.MyButton
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.presentation.core.design_system.scrollbar.vertical.PlatformVerticalScrollbar
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItem
import ru.samtakoy.presentation.core.design_system.toolbar.ToolbarTitleView
import ru.samtakoy.presentation.utils.asA
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
                            modifier = Modifier.padding(horizontal = MyTheme.offsets.screenContentHPadding)
                        )
                        cardState.isQuestion -> CardQuestionView(
                            cardState = cardState,
                            buttons = questionButtons,
                            cardContent = content,
                            onEvent = onEvent,
                            modifier = Modifier.padding(horizontal = MyTheme.offsets.screenContentHPadding)
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
            parts = cardContent.parts,
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
            parts = cardContent.parts,
            hasRevertButton = cardContent.hasRevertButton,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Left
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
    parts: ImmutableList<ContentPart>,
    hasRevertButton: Boolean,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Row(
        modifier = modifier
    ) {
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            TextContent(
                parts,
                textAlign,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .verticalScroll(scrollState)
            )
            if (hasRevertButton) {
                MyRoundButtonView(
                    model = remember {
                        MyRoundButtonUiModel(id = AnyUiId(), icon = MyRoundButtonIcon.Revert, size = MyRoundButtonSize.Large)
                    },
                    onClick = { onEvent(Event.RevertClick) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(MyTheme.offsets.large)
                )
            }
        }

        if (scrollState.canScrollForward || scrollState.canScrollBackward) {
            PlatformVerticalScrollbar(
                scrollState = scrollState,
                modifier = Modifier
                    .fillMaxHeight(),

            )
        }
    }
}

/** Блоки кода и текста */
@Composable
private fun BoxScope.TextContent(
    parts: ImmutableList<ContentPart>,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = MyTheme.offsets.xxsmall,
            alignment = Alignment.CenterVertically
        )
    ) {
        for (part in parts) {
            when (part) {
                is ContentPart.Text -> {
                    CardText(
                        text = part.value,
                        textAlign = textAlign
                    )
                }
                is ContentPart.Code -> {
                    if (part.type == CodeType.Text) {
                        Bounded(
                            codeLabel = part.typeLabel,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CardText(
                                text = part.value.asA(),
                                textAlign = TextAlign.Left
                            )
                        }
                    } else if (part.type == CodeType.AutoParsedKotlin) {
                        CodeBlockTextView(
                            code = part.value,
                            codeType = part.type,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    } else {
                        Bounded(
                            codeLabel = part.typeLabel,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CodeBlockTextView(
                                code = part.value,
                                codeType = part.type,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardText(
    text: AnnotatedString,
    textAlign: TextAlign
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = textAlign,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyLarge,
        fontSize = UsualTextSize
    )
}

/** Отображение в рамочке и с названием */
@Composable
private fun Bounded(
    codeLabel: String,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (codeLabel.isNotBlank()) {
            Text(
                text = codeLabel,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = MyTheme.radiuses.small)
                    .background(
                        color = LangLabelBgColor,
                        shape = getTopRoundedShape(MyTheme.radiuses.small)
                    )
                    /*
                    .drawBehind {
                        val stroke = CodeBorderWidth.toPx()
                        val radius = MyTheme.radiuses.small.toPx()
                        val color = CodeBorderColor

                        // Рисуем путь: от левого низа -> вверх -> дуга -> вправо -> дуга -> вниз
                        val path = Path().apply {
                            moveTo(0f, size.height) // Левый нижний угол (без линии вправо)
                            lineTo(0f, radius)
                            arcTo(
                                rect = Rect(0f, 0f, radius * 2, radius * 2),
                                startAngleDegrees = 180f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false
                            )
                            lineTo(size.width - radius, 0f)
                            arcTo(
                                rect = Rect(size.width - radius * 2, 0f, size.width, radius * 2),
                                startAngleDegrees = 270f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false
                            )
                            lineTo(size.width, size.height)
                        }

                        drawPath(
                            path = path,
                            color = color,
                            style = Stroke(width = stroke)
                        )
                    }*/
                    .padding(start = MyTheme.offsets.small, end = MyTheme.offsets.small, top = MyTheme.offsets.xxsmall),
                textAlign = TextAlign.Center,
                color = LangLabelColor,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = CodeLabelSize
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = CodeBorderWidth,
                    color = CodeBorderColor,
                    shape = getRoundedShape(MyTheme.radiuses.small)
                )
                .padding(horizontal = MyTheme.offsets.small, vertical = MyTheme.offsets.medium),
        ) {
            content()
        }
    }
}

@Stable
private fun resolveSyntax(codeType: CodeType): SyntaxLanguage {
    return when (codeType) {
        // подсвечиваются ключевые слова всех языков
        CodeType.AllKeywords,
        CodeType.Text,
        CodeType.AutoParsedKotlin -> SyntaxLanguage.DEFAULT

        CodeType.Swift -> SyntaxLanguage.SWIFT
        CodeType.Kotlin -> SyntaxLanguage.KOTLIN
        CodeType.Java -> SyntaxLanguage.JAVA
        CodeType.JavaScript -> SyntaxLanguage.JAVASCRIPT
        CodeType.ObjectiveC -> SyntaxLanguage.C
        CodeType.C -> SyntaxLanguage.C
        CodeType.CPP -> SyntaxLanguage.CPP
    }
}

/** Отображение блока кода */
@Composable
private fun CodeBlockTextView(
    code: String,
    codeType: CodeType,
    modifier: Modifier
) {
    val lang: SyntaxLanguage = remember(codeType) {
        resolveSyntax(codeType)
    }

    val highlights by remember {
        mutableStateOf(
            Highlights
                .Builder(code = code)
                .language(lang)
                .build()
        )
    }

    var textState by remember {
        mutableStateOf(AnnotatedString(highlights.getCode()))
    }

    LaunchedEffect(highlights) {
        textState = highlights
            .getHighlights()
            .generateAnnotatedString(highlights.getCode())
    }

    Text(
        modifier = modifier,
        text = textState,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyLarge,
        fontSize = CodeTextSize
    )
}

@Composable
private fun ButtonsRow(
    buttons: ImmutableList<MyButtonUiModel>,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MyTheme.offsets.medium, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        buttons.forEach { buttonModel ->
            key(buttonModel.id) {
                MyButton(
                    model = buttonModel,
                    onClick = {
                        onEvent(Event.ButtonClick(it))
                    }
                )
            }
        }
    }
}

private val LangLabelColor = Color.DarkGray.copy(alpha = 0.95f)
private val LangLabelBgColor = Color.LightGray.copy(alpha = 0.25f)
private val CodeBorderWidth = 2.dp
private val CodeBorderColor = Color.DarkGray.copy(alpha = 0.15f)
private val CodeLabelSize = 16.sp
private val CodeTextSize = 20.sp
private val UsualTextSize = 20.sp
