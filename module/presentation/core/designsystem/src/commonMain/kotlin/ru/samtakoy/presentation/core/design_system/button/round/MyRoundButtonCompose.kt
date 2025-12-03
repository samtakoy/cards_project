package ru.samtakoy.presentation.core.design_system.button.round

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MyRoundButtonView(
    model: MyRoundButtonUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .buttonSize(model.size),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = resolveImage(model.icon),
            contentDescription = "Круглая кнопка"
        )
    }
}

private fun Modifier.buttonSize(size: MyRoundButtonSize): Modifier {
    return when (size) {
        MyRoundButtonSize.Auto -> this
        MyRoundButtonSize.ExtraSmall -> this.size(16.dp)
        MyRoundButtonSize.Small -> this.size(24.dp)
        MyRoundButtonSize.Medium -> this.size(32.dp)
        MyRoundButtonSize.Large -> this.size(64.dp)
    }
}

@Stable
private fun resolveImage(icon: MyRoundButtonIcon): ImageVector {
    return when (icon) {
        MyRoundButtonIcon.Revert -> Icons.Default.Undo
        MyRoundButtonIcon.MediaPlay -> Icons.Default.PlayCircle
        MyRoundButtonIcon.MediaStop -> Icons.Default.StopCircle
        MyRoundButtonIcon.MediaPause -> Icons.Default.PauseCircle
        MyRoundButtonIcon.MediaPrev -> Icons.Default.SkipPrevious
        MyRoundButtonIcon.MediaNext -> Icons.Default.SkipNext
    }
}