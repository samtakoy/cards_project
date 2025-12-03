package ru.samtakoy.oldlegacy.core.presentation.qpack.list.mapper

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.oldlegacy.core.presentation.qpack.list.model.QPackListItemUiModel
import ru.samtakoy.oldlegacy.core.presentation.qpack.list.model.QPackSortType
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.presentation.utils.asAnnotated
import kotlin.time.ExperimentalTime

internal interface QPackListItemUiModelMapper {
    fun map(item: QPack, sortType: QPackSortType): QPackListItemUiModel
    fun mapImmutableList(items: List<QPack>, sortType: QPackSortType): ImmutableList<QPackListItemUiModel>
}

internal class QPackListItemUiModelMapperImpl(): QPackListItemUiModelMapper {
    override fun map(
        item: QPack,
        sortType: QPackSortType
    ): QPackListItemUiModel {
        return QPackListItemUiModel(
            id = LongUiId(item.id),
            title = item.title.asAnnotated(),
            dateText = when (sortType) {
                QPackSortType.LAST_VIEW_DATE_ASC -> item.getLastViewDateAsString()
                QPackSortType.CREATION_DATE_DESC -> item.getCreationDateAsString()
            }.asAnnotated(),
            viewCountText = item.viewCount.toString().asAnnotated()
        )
    }

    override fun mapImmutableList(
        items: List<QPack>,
        sortType: QPackSortType
    ): ImmutableList<QPackListItemUiModel> {
        return items.map {
            map(it, sortType)
        }.toImmutableList()
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