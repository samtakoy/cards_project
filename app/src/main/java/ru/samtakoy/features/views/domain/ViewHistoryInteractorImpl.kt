package ru.samtakoy.features.views.domain

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.utils.CollectionUtils
import ru.samtakoy.features.views.data.local.ViewHistoryRepository
import javax.inject.Inject

internal class ViewHistoryInteractorImpl @Inject constructor(
    private val viewHistoryRepository: ViewHistoryRepository,
    private val cardsInteractor: CardsInteractor
) : ViewHistoryInteractor {

    override suspend fun addNewViewItem(
        qPackId: Long,
        cardIds: List<Long>
    ): ViewHistoryItem {
        return viewHistoryRepository.addNewViewItem(qPackId, cardIds)
    }

    override suspend fun addNewViewItemForPack(
        qPackId: Long,
        shuffleCards: Boolean
    ): ViewHistoryItem {
        val cardIds = cardsInteractor.getQPackCardIds(qPackId)
        return viewHistoryRepository.addNewViewItem(
            qPackId = qPackId,
            cardIds = if (shuffleCards) {
                CollectionUtils.createShuffledIds(cardIds)
            } else {
                cardIds
            }
        )
    }

    override suspend fun addNewViewItemForPack(
        qPackId: Long,
        cardIds: List<Long>,
        shuffleCards: Boolean
    ): ViewHistoryItem {
        val ids = if (shuffleCards) {
            CollectionUtils.createShuffledIds(cardIds)
        } else {
            cardIds
        }
        return viewHistoryRepository.addNewViewItem(qPackId, ids)
    }

    override suspend fun updateViewItem(item: ViewHistoryItem): Boolean {
        return viewHistoryRepository.updateViewItem(item)
    }

    override fun getViewHistoryItemAsFlow(viewItemId: Long): Flow<ViewHistoryItem?> {
        return viewHistoryRepository.getViewHistoryItemAsFlow(viewItemId)
    }

    override fun getViewHistoryItemsAsFlow(): Flow<List<ViewHistoryItem>> {
        return viewHistoryRepository.getViewHistoryItemsAsFlow()
    }

    override fun getLastViewHistoryItemForAsFlow(qPackId: Long): Flow<ViewHistoryItem?> {
        return viewHistoryRepository.getLastViewHistoryItemForAsFlow(qPackId)
    }

    override suspend fun getViewItem(viewId: Long): ViewHistoryItem? {
        return viewHistoryRepository.getViewHistoryItemById(viewId)
    }

    /*
    override fun getOrCreateNewViewItemRx(
        viewId: Long,
        newItemFactory: () -> ViewHistoryItem
    ): Single<ViewHistoryItem> {
        return Single.fromCallable {
            var result: ViewHistoryItem? = if (viewId > 0) {
                viewHistoryRepository.getViewHistoryItemByIdSync(viewId)
            } else {
                null
            }
            if (result == null) {
                result = newItemFactory()
                val resultId = viewHistoryRepository.addViewHistoryItemSync(result)
                result.copy(id = resultId)
            } else {
                result
            }
        }
    }*/

    override fun getViewHistoryItemsWithThemeAsFlow(): Flow<List<ViewHistoryItemWithInfo>> {
        return viewHistoryRepository.getViewHistoryItemsExAsFlow()
    }
}