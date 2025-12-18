package ru.samtakoy.presentation.core.design_system.dialogs.choice

import androidx.compose.foundation.background
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.UiRadiuses
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.base.utils.getRoundedShape
import ru.samtakoy.presentation.core.design_system.button.usual.MyButton
import ru.samtakoy.presentation.core.design_system.radio.MyRadioItemGropView

@Composable
fun MyChoiceDialogView(
    dialogState: MutableState<MyChoiceDialogUiModel?>,
    onButtonClick: ((dialogId: UiId?, buttonId: UiId, selectedItem: UiId?) -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    val dialogModel = dialogState.value
    if (dialogModel != null) {
        val choiceState = remember { mutableStateOf(dialogModel.items) }
        val choiceUpdatedState = rememberUpdatedState(choiceState.value)

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
                    MyRadioItemGropView(
                        items = choiceState,
                        onNewSelect = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MyColors.getScreenBackground(),
                                shape = getRoundedShape(UiRadiuses.innerPanelBg)
                            )
                    )
                    Spacer(Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MyOffsets.small, Alignment.End),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        MyButton(
                            model = dialogModel.firstButton,
                            onClick = {
                                onButtonClick?.invoke(
                                    dialogModel.id,
                                    dialogModel.firstButton.id,
                                    choiceUpdatedState.value.find { it.isSelected }?.id
                                )
                                dialogState.value = null
                            }
                        )
                        dialogModel.secondButton?.let { cancelModel ->
                            MyButton(
                                model = cancelModel,
                                onClick = {
                                    onButtonClick?.invoke(
                                        dialogModel.id,
                                        dialogModel.secondButton.id,
                                        choiceUpdatedState.value.find { it.isSelected }?.id
                                    )
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