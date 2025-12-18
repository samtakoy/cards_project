package ru.samtakoy.domain.card

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.common.transaction.TransactionRepository
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.cardtag.TagInteractor
import kotlin.random.Random

internal class CardInteractorImpl(
    private val tagInteractor: TagInteractor,
    private val cardsRepository: CardsRepository,
    private val transactionRepository: TransactionRepository
) : CardInteractor {

    override suspend fun clearDb() {
        return cardsRepository.clearDb()
    }

    override suspend fun updateCard(card: Card) {
        cardsRepository.updateCard(card)
    }

    override suspend fun addCard(card: Card): Long {
        return cardsRepository.addCard(card)
    }

    override suspend fun getCardQPackId(cardId: Long): Long? {
        return cardsRepository.getCardQPackId(cardId)
    }

    override fun getCardAsFlow(cardId: Long): Flow<Card?> {
        return cardsRepository.getCardAsFlow(cardId)
    }

    override suspend fun deleteCardWithRelations(cardId: Long) {
        transactionRepository.withTransaction {
            tagInteractor.deleteAllTagsFromCard(cardId)
            cardsRepository.deleteCard(cardId)
            // TODO также удалить неиспользуемые теги
        }
    }

    override suspend fun setCardNewQuestionText(cardId: Long, text: String) {
        cardsRepository.getCard(cardId)?.copy(
            question = text
        )?.let {
            cardsRepository.updateCard(it)
        }
    }

    override suspend fun setCardNewAnswerText(cardId: Long, text: String) {
        cardsRepository.getCard(cardId)?.copy(
            answer = text
        )?.let {
            cardsRepository.updateCard(it)
        }
    }

    override suspend fun getQPackCards(qPackId: Long): List<Card> {
        return cardsRepository.getQPackCards(qPackId)
    }

    override suspend fun getQPackCardIds(qPackId: Long): List<Long> {
        return cardsRepository.getCardsIdsFromQPack(qPackId)
    }

    override fun getQPackCardIdsAsFlow(qPackId: Long): Flow<List<Long>> {
        return cardsRepository.getQPackCardIdsAsFlow(qPackId)
    }

    override suspend fun addFakeCard(qPackId: Long) {
        val num = Random.nextInt(10000)
        val card = Card(
            id = 0L,
            qPackId = qPackId,
            question = "fake question $num",
            answer = "fake answer $num",
            aImages = listOf(),
            comment = "comment",
            favorite = 0
        )
        cardsRepository.addCard(card)
    }
}