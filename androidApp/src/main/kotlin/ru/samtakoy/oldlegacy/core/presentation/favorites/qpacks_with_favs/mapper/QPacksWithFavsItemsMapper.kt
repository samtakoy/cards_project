package ru.samtakoy.oldlegacy.core.presentation.favorites.qpacks_with_favs.mapper

import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.model.StringUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.presentation.utils.asAnnotated

interface QPacksWithFavsItemsMapper {
    fun map(item: QPack, isSelected: Boolean): MySelectableItemModel
    fun mapActionButton(): MyButtonUiModel

    companion object {
        const val ACTION_BUTTON_ID = "actionButton"
    }
}

internal class QPacksWithFavsItemsMapperImpl(
    private val resources: Resources
) : QPacksWithFavsItemsMapper {
    override fun map(item: QPack, isSelected: Boolean): MySelectableItemModel {
        return MySelectableItemModel(
            id = LongUiId(item.id),
            text = item.title.asAnnotated(),
            isChecked = isSelected,
            isEnabled = true,
            contentDescription = item.title
        )
    }

    override fun mapActionButton(): MyButtonUiModel {
        return MyButtonUiModel(
            id = StringUiId(QPacksWithFavsItemsMapper.Companion.ACTION_BUTTON_ID),
            text = resources.getString(R.string.qpacks_with_favs_action_button_title).asAnnotated()
        )
    }
}