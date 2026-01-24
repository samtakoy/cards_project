package ru.samtakoy.presentation.qpacks.screens.list.mapper

import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.presentation.base.model.LongUiId
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.presentation.core.appelements.qpacklistitem.QPackListItemUiModel
import ru.samtakoy.presentation.qpacks.screens.list.model.QPackSortType
import ru.samtakoy.presentation.utils.asAnnotated
import kotlin.time.ExperimentalTime

internal interface QPackListItemUiModelMapper {
    fun map(items: List<QPack>, sortType: QPackSortType): List<QPackListItemUiModel>
}

internal class QPackListItemUiModelMapperImpl(): QPackListItemUiModelMapper {

    override fun map(
        items: List<QPack>,
        sortType: QPackSortType
    ): List<QPackListItemUiModel> {
        return items.map {
            mapItem(it, sortType)
        }
    }

    private fun mapItem(
        item: QPack,
        sortType: QPackSortType
    ): QPackListItemUiModel {
        return QPackListItemUiModel(
            id = LongUiId(item.id),
            title = item.title.trim().asAnnotated(),
            creationDate = when (sortType) {
                QPackSortType.LAST_VIEW_DATE_ASC -> item.getLastViewDateAsString()
                QPackSortType.CREATION_DATE_DESC -> item.getCreationDateAsString()
            }.asAnnotated(),
            viewCount = item.viewCount.toString().asAnnotated()
        )
    }

    private fun QPack.getCreationDateAsString(): String {
        @OptIn(ExperimentalTime::class)
        return DateUtils.formatToString(creationDate)
    }

    private fun QPack.getLastViewDateAsString(): String {
        @OptIn(ExperimentalTime::class)
        return DateUtils.formatToString(lastViewDate)
    }
}