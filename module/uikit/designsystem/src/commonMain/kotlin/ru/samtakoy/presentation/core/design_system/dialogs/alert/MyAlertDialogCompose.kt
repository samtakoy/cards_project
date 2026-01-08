package ru.samtakoy.presentation.core.design_system.dialogs.alert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.samtakoy.presentation.core.design_system.base.theme.offsets.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.usual.MyButton

@Composable
fun MyAlertDialogView(
    dialogState: MutableState<MyAlertDialogUiModel?>,
    onButtonClick: (dialogId: UiId?, buttonId: UiId) -> Unit,
    onDismiss: (() -> Unit)? = null
) {
    val dialogModel = dialogState.value
    if (dialogModel != null) {
        Dialog(
            onDismissRequest = {
                dialogState.value = null
                onDismiss?.invoke()
            }
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = MyTheme.offsets.dialogSurfaceElevation,
                modifier = Modifier.padding(MyTheme.offsets.dialogSurfacePaddings)
            ) {
                Column(modifier = Modifier.padding(MyTheme.offsets.dialogContentPaddings)) {
                    Text(
                        text = dialogModel.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    if (dialogModel.description != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(text = dialogModel.description)
                    }
                    Spacer(Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MyTheme.offsets.itemsSmallVOffset, Alignment.End),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        dialogModel.buttons.forEach { buttonModel ->
                            MyButton(
                                model = buttonModel,
                                onClick = {
                                    onButtonClick(dialogModel.id, buttonModel.id)
                                    dialogState.value = null
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}