package ru.samtakoy.presentation.qpacks.screens.info.mapper

import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.utils.R as RUtils
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogUiModel
import ru.samtakoy.presentation.core.design_system.radio.MyRadioItemUiModel
import ru.samtakoy.presentation.qpacks.impl.R
import ru.samtakoy.presentation.utils.asA

internal interface QPackInfoDialogMapper {
    fun mapViewTypeDialog(): MyChoiceDialogUiModel

    companion object {
        val IdChoiceDialog = AnyUiId()
        val IdItemOrdered = AnyUiId()
        val IdItemRandomly = AnyUiId()
        val IdItemInList = AnyUiId()
    }
}

internal class QPackInfoDialogMapperImpl(
    private val resources: Resources
) : QPackInfoDialogMapper {
    override fun mapViewTypeDialog(): MyChoiceDialogUiModel {
        return MyChoiceDialogUiModel(
            id = QPackInfoDialogMapper.Companion.IdChoiceDialog,
            title = resources.getString(R.string.qpack_viewing_types_title).asA(),
            description = null,
            items = listOf<MyRadioItemUiModel>(
                MyRadioItemUiModel(
                    id = QPackInfoDialogMapper.IdItemOrdered,
                    text = resources.getString(R.string.qpack_viewing_type_ordered).asA(),
                    isSelected = true
                ),
                MyRadioItemUiModel(
                    id = QPackInfoDialogMapper.IdItemRandomly,
                    text = resources.getString(R.string.qpack_viewing_type_randomly).asA(),
                    isSelected = false
                ),
                MyRadioItemUiModel(
                    id = QPackInfoDialogMapper.IdItemInList,
                    text = resources.getString(R.string.qpack_viewing_type_inlist).asA(),
                    isSelected = false
                ),
            ).toImmutableList(),
            okButton = MyButtonUiModel(
                id = AnyUiId(),
                text = resources.getString(RUtils.string.action_ok).asA(),
                isEnabled = true
            ),
            cancelButton = MyButtonUiModel(
                id = AnyUiId(),
                text = resources.getString(RUtils.string.action_cancel).asA(),
                isEnabled = true
            )
        )
    }
}