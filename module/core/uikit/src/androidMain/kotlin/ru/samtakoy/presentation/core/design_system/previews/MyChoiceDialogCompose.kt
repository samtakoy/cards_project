package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogView
import ru.samtakoy.presentation.core.design_system.previewutils.getMyRadioPreviewItems
import ru.samtakoy.presentation.utils.asA

@Preview
@Composable
private fun MyChoiceDialogView_Preview() = MyTheme {
    MyChoiceDialogView(
        dialogState = remember {
            mutableStateOf(
                MyChoiceDialogUiModel(
                    id = null,
                    title = "title".asA(),
                    description = "description".asA(),
                    items = getMyRadioPreviewItems(),
                    firstButton = MyButtonUiModel(
                        id = LongUiId(1L),
                        text = "ok".asA(),
                        isEnabled = true
                    )
                )
            )
        },
        onButtonClick = null
    )
}