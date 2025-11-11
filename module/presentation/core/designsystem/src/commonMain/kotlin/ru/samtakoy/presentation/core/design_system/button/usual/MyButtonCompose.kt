package ru.samtakoy.presentation.core.design_system.button.usual

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyButton(
    model: MyButtonUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = model.isEnabled
    ) {
        Text(model.text)
    }
}