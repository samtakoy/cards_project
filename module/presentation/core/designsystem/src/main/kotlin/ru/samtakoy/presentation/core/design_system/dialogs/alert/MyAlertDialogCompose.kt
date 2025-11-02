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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.MyButton
import ru.samtakoy.presentation.core.design_system.button.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.alert.MyAlertDialogUiModel
import ru.samtakoy.presentation.utils.asA

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
                    Spacer(Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MyOffsets.small, Alignment.End),
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

@Preview
@Composable
private fun MyAlertDialogView_Preview() = MyTheme {
    MyAlertDialogView(
        dialogState = remember {
            mutableStateOf(
                MyAlertDialogUiModel(
                    id = null,
                    title = "title".asA(),
                    description = "description".asA(),
                    buttons = listOf<MyButtonUiModel>(
                        MyButtonUiModel(
                            id = LongUiId(1L),
                            text = "ok".asA(),
                            isEnabled = true
                        ),
                        MyButtonUiModel(
                            id = LongUiId(2L),
                            text = "cancel".asA(),
                            isEnabled = true
                        )
                    ).toImmutableList()
                )
            )
        },
        onButtonClick = { a, b -> Unit }
    )
}
