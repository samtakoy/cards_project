package ru.samtakoy.presentation.core.design_system.button.round

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme

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

@Preview
@Composable
private fun MyFabButtonView_Preview() = MyTheme {
    val models = listOf(
        MyFabButtonUiModel(
            id = AnyUiId(),
            icon = MyFabButtonIcon.Revert
        )
    ).toImmutableList()
    Column(
        modifier = Modifier
            .background(MyColors.getScreenBackground())
            .padding(MyOffsets.small),
        verticalArrangement = Arrangement.spacedBy(MyOffsets.small)
    ) {
        models.forEach {
            MyFabButtonView(model = it, onClick = {})
        }
    }
}
