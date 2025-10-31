package ru.samtakoy.domain.card

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.card.domain.model.Card

interface CardInteractor {
    suspend fun clearDb()

    suspend fun updateCard(card: Card)
    suspend fun addCard(card: Card): Long
    suspend fun getCardQPackId(cardId: Long): Long?
    fun getCardAsFlow(cardId: Long): Flow<Card?>

    // TODO make suspend
    suspend fun deleteCardWithRelations(cardId: Long)

    suspend fun setCardNewQuestionText(cardId: Long, text: String)

    suspend fun setCardNewAnswerText(cardId: Long, text: String)

    suspend fun getQPackCards(qPackId: Long): List<Card>

    suspend fun getQPackCardIds(qPackId: Long): List<Long>

    fun getQPackCardIdsAsFlow(qPackId: Long): Flow<List<Long>>

    suspend fun addFakeCard(qPackId: Long)
}