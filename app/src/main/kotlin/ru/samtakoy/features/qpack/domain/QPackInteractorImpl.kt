package ru.samtakoy.features.qpack.domain

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.data.qpack.QPacksRepository
import java.util.Date
import javax.inject.Inject

class QPackInteractorImpl @Inject constructor(
    private val qPacksRepository: QPacksRepository,
    private val cardsRepository: CardsRepository
) : QPackInteractor {
    override suspend fun getQPack(qPackId: Long): QPack? {
        return qPacksRepository.getQPack(qPackId)
    }

    override fun getQPackAsFlow(qPackId: Long): Flow<QPack> {
        return qPacksRepository.getQPackAsFlow(qPackId)
    }

    override suspend fun deleteQPack(qPackId: Long) {
        // TODO transactions
        // TODO придумать, что делать с НЕИСПОЛЬЗУЕМЫМИ тегами карточек, когда удаляется карточка
        // пока связь тег-карточка удаляется каскадно, а теги остаются,
        // получается, надо чистить поштучно
        cardsRepository.deleteQPackCards(qPackId)
        qPacksRepository.deletePack(qPackId)
    }

    override suspend fun updateQPackViewCount(qPackId: Long, currentTime: Date) {
        qPacksRepository.updateQPackViewCount(qPackId, currentTime)
    }

    override fun getChildQPacksAsFlow(themeId: Long): Flow<List<QPack>> {
        return qPacksRepository.getQPacksFromThemeAsFlow(themeId)
    }

    override fun getAllQPacksByLastViewDateAscAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPack>> {
        return if (searchString.isNullOrBlank()) {
            qPacksRepository.getAllQPacksByLastViewDateAscAsFlow(onlyFavorites = onlyFavorites)
        } else {
            qPacksRepository.getAllQPacksByLastViewDateAscFilteredAsFlow(searchString, onlyFavorites = onlyFavorites)
        }
    }

    override fun getAllQPacksByCreationDateDescAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPack>> {
        return if (searchString.isNullOrBlank()) {
            qPacksRepository.getAllQPacksByCreationDateDescAsFlow(onlyFavorites = onlyFavorites)
        } else {
            qPacksRepository.getAllQPacksByCreationDateDescFilteredAsFlow(searchString, onlyFavorites = onlyFavorites)
        }
    }

    override suspend fun getQPacksByIds(ids: List<Long>): List<QPack> {
        return qPacksRepository.getQPacksByIds(ids)
    }

    override suspend fun getQPacksFromThemeCount(themeId: Long): Int {
        return qPacksRepository.getQPacksFromThemeCount(themeId)
    }
}