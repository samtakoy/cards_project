package ru.samtakoy.features.card.domain

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.features.card.data.CardsRepository
import ru.samtakoy.features.database.data.TransactionRepository
import ru.samtakoy.features.card.domain.model.Card
import ru.samtakoy.features.tag.domain.TagInteractor
import java.util.Random
import javax.inject.Inject

class CardsInteractorImpl @Inject constructor(
    private val tagInteractor: TagInteractor,
    private val cardsRepository: CardsRepository,
    private val transactionRepository: TransactionRepository
) : CardsInteractor {

    override suspend fun clearDb() {
        return cardsRepository.clearDb()
    }

    override fun getCardAsFlow(cardId: Long): Flow<Card?> {
        return cardsRepository.getCardAsFlow(cardId)
    }

    override fun deleteCardWithRelationsSync(cardId: Long) {
        transactionRepository.withTransactionSync {
            tagInteractor.deleteAllTagsFromCard(cardId)
            cardsRepository.deleteCard(cardId)
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
        val num = Random().nextInt(10000)
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