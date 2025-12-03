package ru.samtakoy.presentation.core.design_system.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.MyRadiuses
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.base.utils.getLeftRoundedShape
import ru.samtakoy.presentation.core.design_system.base.utils.toPx
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonUiModel
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonView
import ru.samtakoy.presentation.utils.asAnnotated
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Отображает кнопки управлениян на выдвигающейся панельке справа
 * */
@Composable
fun MyPlayerView(
    model: MyPlayerUiModel,
    modifier: Modifier = Modifier,
    onControlClick: (UiId) -> Unit = {}
) {
    val elementSize = TabWidth.toPx() + PanelMinWidth.toPx()
    var cachedLastVisibleState by remember { mutableStateOf<MyPlayerUiModel.State.Visible?>(null) }
    val lastVisibleState = remember(model.state) {
        if (model.state is MyPlayerUiModel.State.Visible) {
            cachedLastVisibleState = model.state
        }
        cachedLastVisibleState
    }

    AnimatedVisibility(
        visible = model.state !is MyPlayerUiModel.State.Invisible,
        modifier = Modifier.fillMaxSize(),
        enter = fadeIn() + slideInHorizontally(initialOffsetX = { elementSize.toInt() }),
        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { elementSize.toInt() }),
    ) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
        ) {
            lastVisibleState?.let { state ->
                PlayerPanelView(
                    heightPx = maxHeight.toPx(),
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.TopEnd),
                    tabContent = {
                        TabViewContent(
                            isPlaying = state.isPlaying,
                            currentCounter = remember(state.currentIndex) {
                                getCardsCounterText(state.currentIndex + 1)
                            }
                        )
                    },
                    panelContent = {
                        PanelContentView(
                            controls = state.controls,
                            onControlClick = onControlClick
                        )
                    }
                )
            }
        }
    }
}

/**
 * Панелька, которую можно выдвигать и задвигать за торчащий Tab
 * */
@Composable
private fun PlayerPanelView(
    heightPx: Float,
    tabContent: @Composable BoxScope.() -> Unit,
    panelContent: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    val tabWidthPx = TabWidth.toPx()
    val tabMinHeightPx = TabMinHeight.toPx()
    val panelMinHeightPx = PanelMinHeight.toPx()
    val panelMinWidthPx = PanelMinWidth.toPx()
    val panelRadiusPx = PanelRadius.toPx()
    val panelOpenXPx = 0f
    val panelCollapsedXPx = remember { mutableFloatStateOf(panelMinWidthPx) }
    // Актуальная высота кнопки
    var tabHeightPx by remember { mutableFloatStateOf(tabMinHeightPx) }
    // Актуальная высота панели с контентом
    val panelHeightPx = remember { mutableFloatStateOf(panelMinHeightPx) }

    // Перетаскивание по вертикали
    var tabDragYOffsetPx by remember { mutableFloatStateOf(heightPx / 2 - tabHeightPx / 2) }
    val panelYOffsetPx by remember {
        derivedStateOf {
            min(
                tabDragYOffsetPx - panelRadiusPx,
                heightPx - panelHeightPx.value
            ).roundToInt()
        }
    }

    // Перетаскивание по горизонтали
    var viewDragXPx by remember {
        mutableFloatStateOf(panelCollapsedXPx.value)
    }
    var isDragging by remember {
        mutableStateOf(false)
    }
    val viewActualXPx by remember {
        derivedStateOf {
            when {
                isDragging -> {
                    viewDragXPx
                }
                viewDragXPx > panelCollapsedXPx.value / 2 -> {
                    panelCollapsedXPx.value
                }
                else -> {
                    panelOpenXPx
                }
            }
        }
    }
    val viewAnimatedXPx = animateFloatAsState(
        targetValue = viewActualXPx,
        animationSpec = tween(100)
    )

    Row(
        modifier = modifier
            .fillMaxHeight()
            .wrapContentWidth(align = Alignment.Start, unbounded = true)
            // Для "expand/collapsing" понельки
            .offset {
                IntOffset(x = viewAnimatedXPx.value.toInt(), y = 0)
            }
    ) {
        // Кнопка-тянучка
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    tabHeightPx = max(tabMinHeightPx, it.size.height.toFloat())
                }
                .offset {
                    IntOffset(x=0, y = tabDragYOffsetPx.toInt())
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            viewDragXPx = viewAnimatedXPx.value
                            isDragging = true
                        },
                        onDragEnd = {
                            isDragging = false
                        },
                        onDragCancel = {
                            isDragging = false
                        }
                    ) { change, dragAmount ->
                        // По вертикали
                        // Смещение, если следовать отцентра объекта до pointer
                        val myYDragAmount = change.position.y - (tabHeightPx / 2)
                        if (myYDragAmount * dragAmount.y > 0) {
                            tabDragYOffsetPx = min(
                                max(panelRadiusPx, tabDragYOffsetPx + dragAmount.y),
                                heightPx - tabHeightPx - panelRadiusPx
                            )
                        }

                        // По горизонтали
                        if (
                            dragAmount.x > 0 && change.position.x > 0 ||
                            dragAmount.x < 0 && change.position.x < tabWidthPx
                        ) {
                            viewDragXPx = min(
                                max(viewDragXPx + dragAmount.x, panelOpenXPx),
                                panelCollapsedXPx.value
                            )
                        }
                    }
                },
            content = tabContent
        )

        // Панель с контентом
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    panelHeightPx.value = max(panelMinHeightPx, it.size.height.toFloat())
                    panelCollapsedXPx.value = max(panelMinWidthPx, it.size.width.toFloat())
                }
                .offset {
                    IntOffset(x = 0, y = panelYOffsetPx)
                },
            content = panelContent
        )
    }
}

@Composable
internal fun TabViewContent(
    isPlaying: Boolean,
    currentCounter: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = TabMinHeight)
            .width(TabWidth)
            .background(
                color = getPanelColor(),
                shape = getLeftRoundedShape(TabRadius)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Хваталка",
            modifier = Modifier.padding(top = TabContentPadding),
            tint = if (isPlaying) {
                Color.Green
            } else {
                MaterialTheme.colorScheme.onSecondary
            }
        )
        Text(
            text = currentCounter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = TabContentPadding),
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.StartEllipsis,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun PanelContentView(
    controls: ImmutableList<MyRoundButtonUiModel>,
    onControlClick: (UiId) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = getPanelColor(),
                shape = getLeftRoundedShape(PanelRadius)
            )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = ContentHContentPadding,
                vertical = ContentVContentPadding,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ControlsOffset, Alignment.CenterVertically)
        ) {
            controls.forEach { button ->
                key(button.id) {
                    val updatedId by rememberUpdatedState(button.id)
                    MyRoundButtonView(
                        model = button,
                        onClick = remember { { onControlClick(updatedId) } }
                    )
                }
            }
        }
    }
}

@Stable
private fun getCardsCounterText(count: Int): AnnotatedString {
    return count.toString().asAnnotated()
}

@Composable
@ReadOnlyComposable
private fun getPanelColor(): Color {
    return MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
}


private val TabWidth = 48.dp
private val TabMinHeight = 48.dp
private val PanelMinWidth = 24.dp
private val PanelMinHeight = 96.dp
private val TabRadius = MyRadiuses.r16
private val PanelRadius = MyRadiuses.r16
private val TabContentPadding = MyOffsets.small
private val ContentHContentPadding = MyOffsets.small
private val ContentVContentPadding = MyOffsets.medium
private val ControlsOffset = MyOffsets.large