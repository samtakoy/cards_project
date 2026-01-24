package ru.samtakoy.presentation.core.design_system.dropdown

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.base.model.AnyUiId
import ru.samtakoy.presentation.base.model.UiId
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel.ItemType

@Immutable
data class DropDownMenuUiModel(
    val menu: Menu,
    val subMenus: ImmutableList<Menu>?
) {

    @Immutable
    data class Menu(
        val id: UiId,
        val items: ImmutableList<ItemType>,
    )

    @Immutable
    sealed interface ItemType {
        val id: UiId

        @Immutable
        data class Item(
            override val id: UiId,
            val title: AnnotatedString
        ) : ItemType

        @Immutable
        class Separator(
            override val id: UiId
        ) : ItemType

        /**
         * @param id идентификатор, совпадающий с id одного элемента из subMenus
         * */
        @Immutable
        data class SubMenu(
            override val id: UiId,
            val title: AnnotatedString
        ) : ItemType
    }
}

@Stable
fun getEmptyMenu(): DropDownMenuUiModel {
    return DropDownMenuUiModel(DropDownMenuUiModel.Menu(AnyUiId(), listOf<ItemType>().toImmutableList()), null)
}