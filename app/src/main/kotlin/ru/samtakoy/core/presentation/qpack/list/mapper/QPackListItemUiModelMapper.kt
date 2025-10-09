package ru.samtakoy.core.presentation.qpack.list.mapper

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.core.presentation.qpack.list.model.QPackListItemUiModel
import ru.samtakoy.core.presentation.qpack.list.model.QPackSortType
import ru.samtakoy.core.utils.DateUtils.DATE_FORMAT
import ru.samtakoy.features.qpack.domain.QPack
import javax.inject.Inject

internal interface QPackListItemUiModelMapper {
    fun map(item: QPack, sortType: QPackSortType): QPackListItemUiModel
    fun mapImmutableList(items: List<QPack>, sortType: QPackSortType): ImmutableList<QPackListItemUiModel>
}

internal class QPackListItemUiModelMapperImpl @Inject constructor(): QPackListItemUiModelMapper {
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
        return DATE_FORMAT.format(creationDate)
    }

    private fun QPack.getLastViewDateAsString(): String {
        return DATE_FORMAT.format(lastViewDate)
    }
}