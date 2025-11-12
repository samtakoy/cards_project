package ru.samtakoy.data.card

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.samtakoy.data.card.mapper.CardEntityMapper
import ru.samtakoy.data.card.mapper.CardWithTagsEntityMapper
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.common.transaction.TransactionRepository
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.card.domain.model.CardWithTags

internal class CardsRepositoryImpl(
    private val db: MyRoomDb,
    private val transactionRepository: TransactionRepository,
    private val cardDao: CardDao,
    private val cardMapper: CardEntityMapper,
    private val cardWithTagsMapper: CardWithTagsEntityMapper
) : CardsRepository {
    override suspend fun clearDb() {
        /* TODO
        transactionRepository.withTransaction {
            db.clearAllTables()
        }
        */
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

    override suspend fun getCardQPackId(cardId: Long): Long? {
        return if (cardId > 0) {
            cardDao.getCardQPackId(cardId)
        } else {
            null
        }
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

    override suspend fun getAllFavoriteCardsIds(): List<Long> {
        return cardDao.getAllFavoriteCardsIds()
    }

    override suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long> {
        return cardDao.getAllFavoriteCardsIdsFromQPacks(qPackIds)
    }

    override suspend fun deleteCard(cardId: Long) {
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

    override suspend fun getQPackCardCount(qPackId: Long): Int {
        return cardDao.getCardCountInQPack(qPackId)
    }


    override suspend fun getQPackCardsWithTags(qPackId: Long): List<CardWithTags> {
        return cardDao.getCardsWithTagsFromQPack(qPackId).map(cardWithTagsMapper::mapToDomain)
    }
}