package ru.samtakoy.core.presentation.favorites.qpacks_with_favs.mapper

import ru.samtakoy.R
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.core.presentation.design_system.base.model.StringUiId
import ru.samtakoy.core.presentation.design_system.button.MyButtonModel
import ru.samtakoy.core.presentation.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.mapper.QPacksWithFavsItemsMapper.Companion.ACTION_BUTTON_ID
import ru.samtakoy.features.qpack.domain.QPack
import javax.inject.Inject

interface QPacksWithFavsItemsMapper {
    fun map(item: QPack, isSelected: Boolean): MySelectableItemModel
    fun mapActionButton(): MyButtonModel

    companion object {
        const val ACTION_BUTTON_ID = "actionButton"
    }
}

internal class QPacksWithFavsItemsMapperImpl @Inject constructor(
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

    override fun mapActionButton(): MyButtonModel {
        return MyButtonModel(
            id = StringUiId(ACTION_BUTTON_ID),
            text = resources.getString(R.string.qpacks_with_favs_action_button_title).asAnnotated()
        )
    }
}