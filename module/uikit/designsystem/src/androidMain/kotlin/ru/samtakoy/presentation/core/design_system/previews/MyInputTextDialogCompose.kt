package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.inputtext.MyInputTextDialogUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.inputtext.MyInputTextDialogView
import ru.samtakoy.presentation.utils.asA

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
        onButtonClick = { a, b -> Unit }
    )
}
