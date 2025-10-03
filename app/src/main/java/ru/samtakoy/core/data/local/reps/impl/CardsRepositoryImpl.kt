package ru.samtakoy.core.data.local.reps.impl

import androidx.room.withTransaction
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.MyRoomDb
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.other.CardIds
import ru.samtakoy.core.data.local.database.room.entities.other.CardWithTags
import ru.samtakoy.core.data.local.reps.CardsRepository
import javax.inject.Inject

class CardsRepositoryImpl @Inject constructor(private val db: MyRoomDb) : CardsRepository {
    override suspend fun clearDb() {
        db.withTransaction {
            db.clearAllTables()
        }
    }

    override fun addCardSync(card: CardEntity): Long {
        return db.cardDao().addCardSync(card)
    }

    override suspend fun addCard(card: CardEntity): Long {
        return db.cardDao().addCard(card)
    }

    override suspend fun getCard(cardId: Long): CardEntity? {
        return db.cardDao().getCard(cardId)
    }

    override fun getCardAsFlow(cardId: Long): Flow<CardEntity?> {
        return db.cardDao().getCardAsFlow(cardId)
    }

    override fun getCardIds(cardId: Long): CardIds? =
            db.cardDao().getCardIds(cardId)

    override fun updateCardSync(card: CardEntity) {
        db.cardDao().updateCardSync(card)
    }

    override suspend fun updateCard(card: CardEntity) {
        db.cardDao().updateCard(card)
    }

    override suspend fun updateCardFavorite(cardId: Long, favorite: Int) {
        db.cardDao().updateFavorite(cardId, favorite)
    }

    override suspend fun getFavoriteCardsCount(): Int {
        return db.cardDao().getFavoriteCardsCount()
    }

    override fun getAllFavoriteCardsIdsRx(): Single<List<Long>> {
        return db.cardDao().getAllFavoriteCardsIdsRx()
    }

    override suspend fun getAllFavoriteCardsIds(): List<Long> {
        return db.cardDao().getAllFavoriteCardsIds()
    }

    override suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long> {
        return db.cardDao().getAllFavoriteCardsIdsFromQPacks(qPackIds)
    }

    override fun deleteCard(cardId: Long) {
        db.cardDao().deleteCard(cardId)
    }

    override suspend fun deleteQPackCards(qPackId: Long) {
        db.cardDao().deleteQPackCards(qPackId)
    }

    override suspend fun getQPackCards(qPackId: Long): List<CardEntity> {
        return db.cardDao().getCardsFromQPack(qPackId)
    }

    override suspend fun getCardsIdsFromQPack(qPackId: Long): List<Long> {
        return db.cardDao().getCardsIdsFromQPack(qPackId)
    }

    override fun getQPackCardCount(qPackId: Long): Int =
            db.cardDao().getCardCountInQPack(qPackId)


    override suspend fun getQPackCardsWithTags(qPackId: Long): List<CardWithTags> {
        return db.cardDao().getCardsWithTagsFRomQPack(qPackId)
    }
}