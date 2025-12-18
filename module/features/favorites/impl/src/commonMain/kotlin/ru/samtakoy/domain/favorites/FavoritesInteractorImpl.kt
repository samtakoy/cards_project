package ru.samtakoy.domain.favorites

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.card.CardsRepository
import ru.samtakoy.domain.qpack.QPacksRepository

class FavoritesInteractorImpl(
    private val cardsRepository: CardsRepository,
    private val qPacksRepository: QPacksRepository
) : FavoritesInteractor {
    override suspend fun getFavoriteCardsCount(): Int {
        return cardsRepository.getFavoriteCardsCount()
    }

    override suspend fun setCardFavorite(cardId: Long, favorite: Int) {
        return cardsRepository.updateCardFavorite(cardId, favorite)
    }

    override suspend fun setQPackFavorite(qPackId: Long, favorite: Int) {
        qPacksRepository.updateQPackFavorite(qPackId, favorite)
    }

    override suspend fun getAllQPacksIdsByCreationDateDescWithFavs(): List<Long> {
        return qPacksRepository.getAllQPacksIdsByCreationDateDescWithFavs()
    }

    override fun getAllQPacksIdsByCreationDateDescWithFavsAsFlow(): Flow<List<Long>> {
        return qPacksRepository.getAllQPacksIdsByCreationDateDescWithFavsAsFlow()
    }

    override suspend fun getAllFavoriteCardsIds(): List<Long> {
        return cardsRepository.getAllFavoriteCardsIds()
    }

    override suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long> {
        return cardsRepository.getAllFavoriteCardsIdsFromQPacks(qPackIds)
    }
}