package ru.samtakoy.core.business

import io.reactivex.Completable
import io.reactivex.Flowable
import ru.samtakoy.core.database.room.entities.CardEntity
import ru.samtakoy.core.database.room.entities.other.CardIds
import ru.samtakoy.core.database.room.entities.other.CardWithTags

interface CardsRepository {

    fun clearDb(): Completable
    fun updateCard(card: CardEntity)
    fun deleteCard(cardId: Long)

    fun addCard(card: CardEntity): Long
    fun getCard(cardId: Long): CardEntity?
    fun getCardRx(cardId: Long): Flowable<CardEntity>
    fun getCardIds(cardId: Long): CardIds?


    fun getQPackCards(qPackId: Long): List<CardEntity>
    fun getCardsIdsFromQPack(qPackId: Long): List<Long>

    fun getQPackCardsWithTags(qPackId: Long): List<CardWithTags>
    fun getQPackCardCount(qPackId: Long): Int


}