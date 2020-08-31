package ru.samtakoy.core.business.impl

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ru.samtakoy.core.business.CardsRepository
import ru.samtakoy.core.database.room.MyRoomDb
import ru.samtakoy.core.database.room.entities.CardEntity
import ru.samtakoy.core.database.room.entities.other.CardIds
import ru.samtakoy.core.database.room.entities.other.CardWithTags

class CardsRepositoryImpl(private val db: MyRoomDb) : CardsRepository {
    override fun clearDb(): Completable {
        return Completable.fromCallable {
            db.clearAllTables()
            true
        }
    }

    override fun addCard(card: CardEntity): Long {
        card.id = db.cardDao().addCard(card)
        return card.id
    }

    override fun getCard(cardId: Long): CardEntity? =
            db.cardDao().getCard(cardId)

    override fun getCardRx(cardId: Long): Flowable<CardEntity> =
            db.cardDao().getCardRx(cardId)

    override fun getCardIds(cardId: Long): CardIds? =
            db.cardDao().getCardIds(cardId)

    override fun updateCard(card: CardEntity) {
        db.cardDao().updateCard(card)
    }

    override fun deleteCard(cardId: Long) {
        db.cardDao().deleteCard(cardId)
    }

    override fun getQPackCards(qPackId: Long): Flowable<List<CardEntity>> =
            db.cardDao().getCardsFromQPack(qPackId)

    override fun getCardsIdsFromQPack(qPackId: Long): List<Long> =
            db.cardDao().getCardsIdsFromQPack(qPackId)

    override fun getQPackCardCount(qPackId: Long): Int =
            db.cardDao().getCardCountInQPack(qPackId)


    override fun getQPackCardsWithTagsRx(qPackId: Long): Single<List<CardWithTags>> =
            db.cardDao().getCardsWithTagsFRomQPackRx(qPackId)
}