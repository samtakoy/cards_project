package ru.samtakoy.features.views.presentation.history.mapper

import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.domain.view.ViewHistoryItemWithInfo
import ru.samtakoy.features.views.presentation.history.components.OneViewHistoryItemModel
import ru.samtakoy.presentation.utils.asAnnotated
import kotlin.time.ExperimentalTime

internal interface ViewHistoryItemUiModelMapper {
    fun map(item: ViewHistoryItemWithInfo): OneViewHistoryItemModel
}

internal class ViewHistoryItemUiModelMapperImpl(
    private val resources: Resources
) : ViewHistoryItemUiModelMapper {
    override fun map(item: ViewHistoryItemWithInfo): OneViewHistoryItemModel {

        @OptIn(ExperimentalTime::class)
        return OneViewHistoryItemModel(
            id = item.viewItem.id,
            qPackId = item.viewItem.qPackId,
            themeTitle = (
                item.themeTitle ?: resources.getString(R.string.views_history_empty_theme_title)
            ).asAnnotated(),
            packTitle = item.qPackTitle.asAnnotated(),
            viewDate = DateUtils.formatToString(item.viewItem.lastViewDate).asAnnotated(),
            viewedInfo = resources.getString(
                R.string.views_history_empty_viewed_count,
                item.viewItem.viewedCardIds.size,
                item.viewItem.viewedCardIds.size + item.viewItem.todoCardIds.size
            ).asAnnotated(),
            errorCount = resources.getString(
                R.string.views_history_empty_errors_count,
                item.viewItem.errorCardIds.size
            ).asAnnotated()
        )
    }
}