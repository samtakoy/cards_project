package ru.samtakoy.features.views.presentation.history.mapper

import ru.samtakoy.R
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.utils.DateUtils
import ru.samtakoy.features.views.domain.ViewHistoryItemWithInfo
import ru.samtakoy.features.views.presentation.history.components.OneViewHistoryItemModel
import javax.inject.Inject

internal interface ViewHistoryItemUiModelMapper {
    fun map(item: ViewHistoryItemWithInfo): OneViewHistoryItemModel
}

internal class ViewHistoryItemUiModelMapperImpl @Inject constructor(
    private val resources: Resources
) : ViewHistoryItemUiModelMapper {
    override fun map(item: ViewHistoryItemWithInfo): OneViewHistoryItemModel {

        return OneViewHistoryItemModel(
            id = item.viewItem.id,
            qPackId = item.viewItem.qPackId,
            themeTitle = (
                item.themeTitle ?: resources.getString(R.string.views_history_empty_theme_title)
            ).asAnnotated(),
            packTitle = item.qPackTitle.asAnnotated(),
            viewDate = DateUtils.DATE_FORMAT.format(item.viewItem.lastViewDate).asAnnotated(),
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