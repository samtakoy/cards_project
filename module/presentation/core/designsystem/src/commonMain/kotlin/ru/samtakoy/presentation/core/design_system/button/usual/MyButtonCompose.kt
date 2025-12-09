package ru.samtakoy.presentation.core.design_system.button.usual

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import ru.samtakoy.presentation.core.design_system.base.model.UiId

@Composable
fun MyButton(
    model: MyButtonUiModel,
    onClick: (UiId) -> Unit,
    modifier: Modifier = Modifier
) {
    val updatedModel = rememberUpdatedState(model)
    Button(
        onClick = remember { { onClick(updatedModel.value.id) } },
        modifier = modifier,
        enabled = model.isEnabled
    ) {
        Text(model.text)
    }
}