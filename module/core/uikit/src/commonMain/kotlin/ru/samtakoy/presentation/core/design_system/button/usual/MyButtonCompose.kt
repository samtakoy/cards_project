package ru.samtakoy.presentation.core.design_system.button.usual

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.samtakoy.presentation.base.model.UiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.base.utils.getRoundedShape

@Composable
fun MyButton(
    model: MyButtonUiModel,
    onClick: (UiId) -> Unit,
    modifier: Modifier = Modifier
) {
    val minInteractiveSize = if (model.type == MyButtonUiModel.Type.SmallSwitcher) {
        ButtonMinHeight
    } else {
        LocalMinimumInteractiveComponentSize.current
    }

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides minInteractiveSize
    ) {
        Button(
            onClick = { onClick(model.id) },
            modifier = modifier
                .buttonHModifier(model.type),
            enabled = model.isEnabled,
            shape = rememberButtonShape(model.type),
            colors = getButtonColors(model.type),
            contentPadding = rememberButtonContentPadding(model.type)
        ) {
            Text(
                text = model.text,
                fontSize = getButtonTextSize(model.type)
            )
        }
    }
}

@Composable
private fun getButtonColors(type: MyButtonUiModel.Type): ButtonColors = when (type) {
    MyButtonUiModel.Type.SmallSwitcher -> ButtonDefaults.filledTonalButtonColors()
    MyButtonUiModel.Type.Regular -> ButtonDefaults.buttonColors()
}

private fun getButtonTextSize(type: MyButtonUiModel.Type): TextUnit = when (type) {
    MyButtonUiModel.Type.SmallSwitcher -> SmallButtonTextSize
    MyButtonUiModel.Type.Regular -> TextUnit.Unspecified
}

@Composable
private fun rememberButtonShape(type: MyButtonUiModel.Type): Shape {
    val radiusesMedium = MyTheme.radiuses.medium
    val defaultButtonShape = ButtonDefaults.shape
    return remember(type, radiusesMedium, defaultButtonShape) {
        when (type) {
            MyButtonUiModel.Type.SmallSwitcher -> getRoundedShape(radiusesMedium)
            MyButtonUiModel.Type.Regular -> defaultButtonShape
        }
    }
}

@Composable
private fun rememberButtonContentPadding(type: MyButtonUiModel.Type): PaddingValues {
    val smallButtonHPadding = MyTheme.offsets.medium
    val smallButtonVPadding = MyTheme.offsets.xsmall
    return remember(type, smallButtonHPadding, smallButtonVPadding) {
        when (type) {
            MyButtonUiModel.Type.SmallSwitcher -> {
                PaddingValues(horizontal = smallButtonHPadding, vertical = smallButtonVPadding)
            }
            MyButtonUiModel.Type.Regular -> ButtonDefaults.ContentPadding
        }
    }
}

@Stable
private fun Modifier.buttonHModifier(type: MyButtonUiModel.Type): Modifier {
    return if (type == MyButtonUiModel.Type.SmallSwitcher) {
        this.defaultMinSize(minHeight = SmallButtonMinHeight) // Сбрасываем стандартный минимум M3
    } else {
        this
    }
}

private val SmallButtonMinHeight = 32.dp
private val SmallButtonTextSize = 14.sp
private val ButtonMinHeight = SmallButtonMinHeight