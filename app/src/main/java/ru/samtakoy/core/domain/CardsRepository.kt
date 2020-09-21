package ru.samtakoy.core.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.other.CardIds
import ru.samtakoy.core.data.local.database.room.entities.other.CardWithTags

interface CardsRepository {

    fun clearDb(): Completable
    fun updateCard(card: CardEntity)
    fun deleteCard(cardId: Long)
    fun deleteQPackCards(qPackId: Long): Completable

    fun addCard(card: CardEntity): Long
    fun getCard(cardId: Long): CardEntity?
    fun getCardRx(cardId: Long): Flowable<CardEntity>
    fun getCardIds(cardId: Long): CardIds?


    fun getQPackCards(qPackId: Long): Flowable<List<CardEntity>>
    fun getCardsIdsFromQPack(qPackId: Long): Single<List<Long>>

    fun getQPackCardsWithTagsRx(qPackId: Long): Single<List<CardWithTags>>
    fun getQPackCardCount(qPackId: Long): Int


}