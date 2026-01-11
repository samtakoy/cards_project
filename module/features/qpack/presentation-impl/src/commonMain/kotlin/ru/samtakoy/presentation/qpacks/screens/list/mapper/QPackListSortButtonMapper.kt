package ru.samtakoy.presentation.qpacks.screens.list.mapper

import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.coroutines.SuspendLazy
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.qpacks.screens.list.model.QPackListSortButtonId
import ru.samtakoy.presentation.qpacks.screens.list.model.QPackSortType
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.qpacks_menu_item_by_creation_date
import ru.samtakoy.resources.qpacks_menu_item_by_last_view_date

internal interface QPackListSortButtonMapper {
    suspend fun mapSortButton(sortType: QPackSortType): MyButtonUiModel
}

internal class QPackListSortButtonMapperImpl : QPackListSortButtonMapper {
    override suspend fun mapSortButton(sortType: QPackSortType): MyButtonUiModel {
        return when(sortType) {
            QPackSortType.LAST_VIEW_DATE_ASC -> lastViewDateAscBtn.getValue()
            QPackSortType.CREATION_DATE_DESC -> creationDateDescBtn.getValue()
        }
    }

    private val lastViewDateAscBtn = SuspendLazy {
        MyButtonUiModel(
            id = QPackListSortButtonId.LastViewDateAsc,
            text = getString(Res.string.qpacks_menu_item_by_last_view_date).asA(),
            isEnabled = true,
            type = MyButtonUiModel.Type.SmallSwitcher
        )
    }
    private val creationDateDescBtn = SuspendLazy {
        MyButtonUiModel(
            id = QPackListSortButtonId.CreationDateDesc,
            text = getString(Res.string.qpacks_menu_item_by_creation_date).asA(),
            isEnabled = true,
            type = MyButtonUiModel.Type.SmallSwitcher
        )
    }
}