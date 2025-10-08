package ru.samtakoy.features.card.domain

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.features.card.domain.model.Card

interface CardsInteractor {
    suspend fun clearDb()

    fun getCardAsFlow(cardId: Long): Flow<Card?>

    // TODO make suspend
    fun deleteCardWithRelationsSync(cardId: Long)

    suspend fun setCardNewQuestionText(cardId: Long, text: String)

    suspend fun setCardNewAnswerText(cardId: Long, text: String)

    suspend fun getQPackCards(qPackId: Long): List<Card>

    suspend fun getQPackCardIds(qPackId: Long): List<Long>

    fun getQPackCardIdsAsFlow(qPackId: Long): Flow<List<Long>>

    suspend fun addFakeCard(qPackId: Long)
}