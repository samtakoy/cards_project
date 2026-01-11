package ru.samtakoy.domain.qpack

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.card.CardsRepository
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal class QPackInteractorImpl(
    private val qPackRepository: QPacksRepository,
    private val cardsRepository: CardsRepository
) : QPackInteractor {
    override suspend fun getQPack(qPackId: Long): QPack? {
        return qPackRepository.getQPack(qPackId)
    }

    override fun getQPackAsFlow(qPackId: Long): Flow<QPack?> {
        return qPackRepository.getQPackAsFlow(qPackId)
    }

    override suspend fun addQPack(qPack: QPack): Long {
        return qPackRepository.addQPack(qPack)
    }

    override suspend fun updateQPack(qPack: QPack) {
        qPackRepository.updateQPack(qPack)
    }

    override suspend fun deleteQPack(qPackId: Long) {
        // TODO transactions
        // TODO придумать, что делать с НЕИСПОЛЬЗУЕМЫМИ тегами карточек, когда удаляется карточка
        // пока связь тег-карточка удаляется каскадно, а теги остаются,
        // получается, надо чистить поштучно
        cardsRepository.deleteQPackCards(qPackId)
        qPackRepository.deletePack(qPackId)
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun updateQPackViewCount(qPackId: Long, currentTime: Instant) {
        qPackRepository.updateQPackViewCount(qPackId, currentTime)
    }

    override fun getChildQPacksAsFlow(themeId: Long): Flow<List<QPack>> {
        return qPackRepository.getQPacksFromThemeAsFlow(themeId)
    }

    override suspend fun getQPacksByIds(ids: List<Long>): List<QPack> {
        return qPackRepository.getQPacksByIds(ids)
    }

    override suspend fun getQPacksFromThemeCount(themeId: Long): Int {
        return qPackRepository.getQPacksFromThemeCount(themeId)
    }
}