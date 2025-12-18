package ru.samtakoy.domain.learncourse

import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.domain.view.ViewHistoryItem
import kotlin.time.ExperimentalTime

internal class ViewHistoryProgressUseCaseImpl(
    private val qPackInteractor: QPackInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val courseProgressUseCase: CourseProgressUseCase,
) : ViewHistoryProgressUseCase {

    @OptIn(ExperimentalTime::class)
    override suspend fun rollbackToPrevCard(viewItemId: Long): Long? {
        val item = viewHistoryInteractor.getViewItem(viewItemId)
        if (item == null || item.viewedCardIds.isEmpty()) {
            return null
        }
        val newCurrentCardId: Long = item.viewedCardIds.last()
        viewHistoryInteractor.updateViewItem(
            item.copy(
                viewedCardIds = item.viewedCardIds.subList(0, item.viewedCardIds.lastIndex),
                todoCardIds = listOf(item.viewedCardIds.last()) + item.todoCardIds,
                lastViewDate = DateUtils.currentTimeDate
            )
        )
        return newCurrentCardId
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun makeViewedCurCard(
        viewItemId: Long,
        curCardId: Long,
        withError: Boolean
    ): ViewHistoryItem? {
        val item = viewHistoryInteractor.getViewItem(viewItemId)
        if (item == null || item.todoCardIds.isEmpty()) {
            return null
        }
        val transferCardId = item.todoCardIds.first()
        if (transferCardId != curCardId) {
            return null
        }
        val isLastCard = item.todoCardIds.size == 1

        val resultItem = item.copy(
            viewedCardIds = item.viewedCardIds + transferCardId,
            todoCardIds = item.todoCardIds.filter { it != transferCardId },
            errorCardIds = if (withError && item.errorCardIds.contains(transferCardId).not()) {
                item.errorCardIds + transferCardId
            } else {
                item.errorCardIds
            },
            lastViewDate = DateUtils.currentTimeDate
        )

        viewHistoryInteractor.updateViewItem(item = resultItem)

        if (isLastCard) {
            if (item.qPackId > 0) {
                qPackInteractor.updateQPackViewCount(item.qPackId, DateUtils.currentTimeDate)
            }
            courseProgressUseCase.finishCourseCardsViewingForViewId(
                viewId = viewItemId,
                currentTime = DateUtils.currentTimeDate
            )
        }
        return resultItem
    }
}