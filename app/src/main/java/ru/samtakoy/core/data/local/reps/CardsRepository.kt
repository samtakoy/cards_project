package ru.samtakoy.core.data.local.reps

import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.other.CardIds
import ru.samtakoy.core.data.local.database.room.entities.other.CardWithTags

interface CardsRepository {

    suspend fun clearDb()
    fun updateCardSync(card: CardEntity)
    suspend fun updateCard(card: CardEntity)
    fun deleteCard(cardId: Long)
    suspend fun deleteQPackCards(qPackId: Long)

    fun addCardSync(card: CardEntity): Long
    suspend fun addCard(card: CardEntity): Long
    suspend fun getCard(cardId: Long): CardEntity?
    fun getCardAsFlow(cardId: Long): Flow<CardEntity?>
    fun getCardIds(cardId: Long): CardIds?

    // favorites
    suspend fun updateCardFavorite(cardId: Long, favorite: Int)
    suspend fun getFavoriteCardsCount(): Int
    fun getAllFavoriteCardsIdsRx(): Single<List<Long>>
    suspend fun getAllFavoriteCardsIds(): List<Long>
    suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long>

    suspend fun getQPackCards(qPackId: Long): List<CardEntity>

    suspend fun getCardsIdsFromQPack(qPackId: Long): List<Long>

    suspend fun getQPackCardsWithTags(qPackId: Long): List<CardWithTags>
    fun getQPackCardCount(qPackId: Long): Int
}