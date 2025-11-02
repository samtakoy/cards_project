package ru.samtakoy.presentation.qpacks.screens.info.mapper

import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.qpacks.impl.R
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel.ItemType
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel.Menu
import ru.samtakoy.presentation.utils.asAnnotated

internal interface QPackInfoMenuMapper {
    fun map(): DropDownMenuUiModel

    companion object {
        val IdMenu = AnyUiId()
        val IdItemDeletePack = AnyUiId()
        val IdItemAddFakeCard = AnyUiId()
    }
}

internal class QPackInfoMenuMapperImpl(
    private val resources: Resources
) : QPackInfoMenuMapper {
    override fun map(): DropDownMenuUiModel {
        return DropDownMenuUiModel(
            menu = Menu(
                id = QPackInfoMenuMapper.IdMenu,
                items = buildItems().toImmutableList(),
            ),
            subMenus = emptyList<Menu>().toImmutableList()
        )
    }

    private fun buildItems(): List<ItemType> {
        return buildList {
            add(
                ItemType.Item(
                    id = QPackInfoMenuMapper.IdItemDeletePack,
                    title = resources.getString(R.string.qpack_menu_delete).asAnnotated()
                )
            )
            add(
                ItemType.Item(
                    id = QPackInfoMenuMapper.IdItemAddFakeCard,
                    title = resources.getString(R.string.qpack_menu_add_fake_card).asAnnotated()
                )
            )
        }
    }
}