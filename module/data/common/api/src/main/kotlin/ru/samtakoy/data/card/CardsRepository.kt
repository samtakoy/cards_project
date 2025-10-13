package ru.samtakoy.data.card

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.card.domain.model.CardWithTags

interface CardsRepository {

    suspend fun clearDb()
    fun updateCardSync(card: Card)
    suspend fun updateCard(card: Card)
    fun deleteCard(cardId: Long)
    suspend fun deleteQPackCards(qPackId: Long)

    fun addCardSync(card: Card): Long
    suspend fun addCard(card: Card): Long
    suspend fun getCard(cardId: Long): Card?
    fun getCardAsFlow(cardId: Long): Flow<Card?>
    fun getCardQPackId(cardId: Long): Long?

    // favorites
    suspend fun updateCardFavorite(cardId: Long, favorite: Int)
    suspend fun getFavoriteCardsCount(): Int
    suspend fun getAllFavoriteCardsIds(): List<Long>
    suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long>

    suspend fun getQPackCards(qPackId: Long): List<Card>

    suspend fun getCardsIdsFromQPack(qPackId: Long): List<Long>
    fun getQPackCardIdsAsFlow(qPackId: Long): Flow<List<Long>>

    suspend fun getQPackCardsWithTags(qPackId: Long): List<CardWithTags>
    fun getQPackCardCount(qPackId: Long): Int
}