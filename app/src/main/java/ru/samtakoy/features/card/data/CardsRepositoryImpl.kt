package ru.samtakoy.features.card.data

import androidx.room.withTransaction
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.samtakoy.features.database.data.MyRoomDb
import ru.samtakoy.features.card.data.mapper.CardEntityMapper
import ru.samtakoy.features.card.data.mapper.CardWithTagsEntityMapper
import ru.samtakoy.features.card.domain.model.Card
import ru.samtakoy.features.card.domain.model.CardWithTags
import javax.inject.Inject

internal class CardsRepositoryImpl @Inject constructor(
    private val db: MyRoomDb,
    private val cardDao: CardDao,
    private val cardMapper: CardEntityMapper,
    private val cardWithTagsMapper: CardWithTagsEntityMapper
) : CardsRepository {
    override suspend fun clearDb() {
        // TODO?
        db.withTransaction {
            db.clearAllTables()
        }
    }

    override fun addCardSync(card: Card): Long {
        return cardDao.addCardSync(cardMapper.mapToEntity(card))
    }

    override suspend fun addCard(card: Card): Long {
        return cardDao.addCard(cardMapper.mapToEntity(card))
    }

    override suspend fun getCard(cardId: Long): Card? {
        return cardDao.getCard(cardId)?.let(cardMapper::mapToDomain)
    }

    override fun getCardAsFlow(cardId: Long): Flow<Card?> {
        return cardDao.getCardAsFlow(cardId).map { it?.let(cardMapper::mapToDomain) }
    }

    override fun getCardQPackId(cardId: Long): Long? {
        return if (cardId > 0) {
            cardDao.getCardQPackId(cardId)
        } else {
            null
        }
    }

    override fun updateCardSync(card: Card) {
        cardDao.updateCardSync(cardMapper.mapToEntity(card))
    }

    override suspend fun updateCard(card: Card) {
        cardDao.updateCard(cardMapper.mapToEntity(card))
    }

    override suspend fun updateCardFavorite(cardId: Long, favorite: Int) {
        cardDao.updateFavorite(cardId, favorite)
    }

    override suspend fun getFavoriteCardsCount(): Int {
        return cardDao.getFavoriteCardsCount()
    }

    override fun getAllFavoriteCardsIdsRx(): Single<List<Long>> {
        return cardDao.getAllFavoriteCardsIdsRx()
    }

    override suspend fun getAllFavoriteCardsIds(): List<Long> {
        return cardDao.getAllFavoriteCardsIds()
    }

    override suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long> {
        return cardDao.getAllFavoriteCardsIdsFromQPacks(qPackIds)
    }

    override fun deleteCard(cardId: Long) {
        cardDao.deleteCard(cardId)
    }

    override suspend fun deleteQPackCards(qPackId: Long) {
        cardDao.deleteQPackCards(qPackId)
    }

    override suspend fun getQPackCards(qPackId: Long): List<Card> {
        return cardDao.getCardsFromQPack(qPackId).map(cardMapper::mapToDomain)
    }

    override suspend fun getCardsIdsFromQPack(qPackId: Long): List<Long> {
        return cardDao.getCardsIdsFromQPack(qPackId)
    }

    override fun getQPackCardIdsAsFlow(qPackId: Long): Flow<List<Long>> {
        return cardDao.getCardsIdsFromQPackAsFlow(qPackId)
    }

    override fun getQPackCardCount(qPackId: Long): Int {
        return cardDao.getCardCountInQPack(qPackId)
    }


    override suspend fun getQPackCardsWithTags(qPackId: Long): List<CardWithTags> {
        return cardDao.getCardsWithTagsFromQPack(qPackId).map(cardWithTagsMapper::mapToDomain)
    }
}