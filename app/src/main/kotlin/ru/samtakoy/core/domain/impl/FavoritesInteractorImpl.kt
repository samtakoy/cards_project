package ru.samtakoy.core.domain.impl

import io.reactivex.Completable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx2.rxCompletable
import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.core.domain.FavoritesInteractor
import ru.samtakoy.data.qpack.QPacksRepository
import javax.inject.Inject

class FavoritesInteractorImpl @Inject constructor(
    private val cardsRepository: CardsRepository,
    private val qPacksRepository: QPacksRepository
) : FavoritesInteractor {
    override suspend fun getFavoriteCardsCount(): Int {
        return cardsRepository.getFavoriteCardsCount()
    }

    override fun setCardFavoriteRx(cardId: Long, favorite: Int): Completable {
        return rxCompletable {
            cardsRepository.updateCardFavorite(cardId, favorite)
            true
        }
    }

    override suspend fun setCardFavorite(cardId: Long, favorite: Int) {
        return cardsRepository.updateCardFavorite(cardId, favorite)
    }

    override fun setQPackFavorite(qPackId: Long, favorite: Int): Completable {
        return Completable.fromCallable {
            qPacksRepository.updateQPackFavorite(qPackId, favorite)
            true
        }
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