package ru.samtakoy.presentation.core.design_system.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.UiRadiuses
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.base.utils.getRoundedShape
import ru.samtakoy.presentation.core.design_system.base.utils.toDp
import ru.samtakoy.presentation.utils.asA

@Composable
fun ProgressOverlayView(
    model: ProgressOverlayUiModel,
    modifier: Modifier = Modifier
) {
    ProgressOverlayView(
        title = model.title,
        subtitle = model.subtitle,
        modifier = modifier
    )
}


@Composable
fun ProgressOverlayView(
    title: AnnotatedString,
    subtitle: AnnotatedString?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MyColors.getOverlayColor())
            .pointerInput(Unit) {}
    ) {
        Surface(
            modifier = modifier
                .padding(
                    horizontal = UiOffsets.screenContentHPadding,
                    vertical = UiOffsets.screenContentVPadding
                )
                .wrapContentSize()
                .clip(getRoundedShape(UiRadiuses.windowPanelBg))
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .defaultMinSize(
                        minWidth = LocalWindowInfo.current.containerSize.width.toDp() * .6f
                    )
                    .padding(
                        horizontal = UiOffsets.screenContentHPadding,
                        vertical = UiOffsets.screenContentVPadding
                    )
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Left,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleLarge
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProgressOverlayView_Preview() = MyTheme {
    ProgressOverlayView(
        title = "title".asA(),
        subtitle = "subtitle".asA()
    )
}