package ru.samtakoy.presentation.core.design_system.button.round

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MyFabButtonView(
    model: MyFabButtonUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(64.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = resolveImage(model.icon),
            contentDescription = "Круглая кнопка"
        )
    }
}

@Stable
private fun resolveImage(icon: MyFabButtonIcon): ImageVector {
    return when (icon) {
        MyFabButtonIcon.Revert -> Icons.Default.ArrowBack
    }
}