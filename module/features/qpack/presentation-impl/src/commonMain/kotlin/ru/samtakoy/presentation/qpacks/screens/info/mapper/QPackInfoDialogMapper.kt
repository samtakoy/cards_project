package ru.samtakoy.presentation.qpacks.screens.info.mapper

import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.getString
import ru.samtakoy.presentation.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogUiModel
import ru.samtakoy.presentation.core.design_system.radio.MyRadioItemUiModel
import ru.samtakoy.presentation.qpacks.screens.info.model.ChoiceButtonId
import ru.samtakoy.presentation.qpacks.screens.info.model.DialogId
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.action_cancel
import ru.samtakoy.resources.action_ok
import ru.samtakoy.resources.qpack_viewing_type_inlist
import ru.samtakoy.resources.qpack_viewing_type_ordered
import ru.samtakoy.resources.qpack_viewing_type_randomly
import ru.samtakoy.resources.qpack_viewing_types_title

internal interface QPackInfoDialogMapper {
    suspend fun mapViewTypeDialog(): MyChoiceDialogUiModel

    companion object {
        // айтемы выбора
        val IdItemOrdered = AnyUiId()
        val IdItemRandomly = AnyUiId()
        val IdItemInList = AnyUiId()
    }
}

internal class QPackInfoDialogMapperImpl : QPackInfoDialogMapper {
    override suspend fun mapViewTypeDialog(): MyChoiceDialogUiModel {
        return MyChoiceDialogUiModel(
            id = DialogId.ViewChoice,
            title = getString(Res.string.qpack_viewing_types_title).asA(),
            description = null,
            items = listOf<MyRadioItemUiModel>(
                MyRadioItemUiModel(
                    id = QPackInfoDialogMapper.IdItemOrdered,
                    text = getString(Res.string.qpack_viewing_type_ordered).asA(),
                    isSelected = true
                ),
                MyRadioItemUiModel(
                    id = QPackInfoDialogMapper.IdItemRandomly,
                    text = getString(Res.string.qpack_viewing_type_randomly).asA(),
                    isSelected = false
                ),
                MyRadioItemUiModel(
                    id = QPackInfoDialogMapper.IdItemInList,
                    text = getString(Res.string.qpack_viewing_type_inlist).asA(),
                    isSelected = false
                ),
            ).toImmutableList(),
            firstButton = MyButtonUiModel(
                id = ChoiceButtonId.Cancel,
                text = getString(Res.string.action_cancel).asA(),
                isEnabled = true
            ),
            secondButton = MyButtonUiModel(
                id = ChoiceButtonId.Ok,
                text = getString(Res.string.action_ok).asA(),
                isEnabled = true
            )
        )
    }
}