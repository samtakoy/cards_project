package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.alert.MyAlertDialogUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.alert.MyAlertDialogView
import ru.samtakoy.presentation.utils.asA

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
