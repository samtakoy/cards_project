package ru.samtakoy.data.card

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.card.domain.model.CardWithTags

// TODO интерфейсы репозиториев перенести в domain в repository-api модули?
interface CardsRepository {

    suspend fun clearDb()
    suspend fun updateCard(card: Card)
    suspend fun deleteCard(cardId: Long)
    suspend fun deleteQPackCards(qPackId: Long)

    suspend fun addCard(card: Card): Long
    suspend fun getCard(cardId: Long): Card?
    fun getCardAsFlow(cardId: Long): Flow<Card?>
    suspend fun getCardQPackId(cardId: Long): Long?

    // favorites
    suspend fun updateCardFavorite(cardId: Long, favorite: Int)
    suspend fun getFavoriteCardsCount(): Int
    suspend fun getAllFavoriteCardsIds(): List<Long>
    suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long>

    suspend fun getQPackCards(qPackId: Long): List<Card>
    suspend fun getCards(cardIds: List<Long>): List<Card>

    suspend fun getCardsIdsFromQPack(qPackId: Long): List<Long>
    fun getQPackCardIdsAsFlow(qPackId: Long): Flow<List<Long>>

    suspend fun getQPackCardsWithTags(qPackId: Long): List<CardWithTags>
    suspend fun getQPackCardCount(qPackId: Long): Int
}