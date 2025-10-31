package ru.samtakoy.presentation.core.design_system.dialogs.inputtext

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.MyButton
import ru.samtakoy.presentation.core.design_system.button.MyButtonUiModel
import ru.samtakoy.presentation.utils.asA

@Composable
fun MyInputTextDialogView(
    dialogState: MutableState<MyInputTextDialogUiModel?>,
    onOkClick: (dialogId: UiId?, result: String) -> Unit,
    onDismiss: (() -> Unit)? = null
) {
    val dialogModel = dialogState.value
    if (dialogModel != null) {
        var inputText by remember(dialogModel) { mutableStateOf(dialogModel.initialText) }

        Dialog(
            onDismissRequest = {
                dialogState.value = null
                onDismiss?.invoke()
            }
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = UiOffsets.dialogSurfaceElevation,
                modifier = Modifier.padding(UiOffsets.dialogSurfacePaddings)
            ) {
                Column(modifier = Modifier.padding(UiOffsets.dialogContentPaddings)) {
                    Text(
                        text = dialogModel.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    if (dialogModel.description != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(text = dialogModel.description)
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { newText -> inputText = newText },
                        label = dialogModel.inputHint?.let { { Text(dialogModel.inputHint) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MyButton(
                            model = dialogModel.okButton,
                            onClick = {
                                onOkClick(dialogModel.id, inputText)
                                dialogState.value = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyInputTextDialogView_Preview() = MyTheme {
    MyInputTextDialogView(
        dialogState = remember {
            mutableStateOf(
                MyInputTextDialogUiModel(
                    id = null,
                    title = "title".asA(),
                    description = "description".asA(),
                    inputHint = "inputHint".asA(),
                    initialText = "а...",
                    okButton = MyButtonUiModel(
                        id = LongUiId(1L),
                        text = "ok".asA(),
                        isEnabled = true
                    )
                )
            )
        },
        onOkClick = { a, b -> Unit }
    )
}
