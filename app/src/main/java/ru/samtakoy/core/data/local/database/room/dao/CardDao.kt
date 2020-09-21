package ru.samtakoy.core.data.local.database.room.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion._id
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion._qpack_id
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion.table
import ru.samtakoy.core.data.local.database.room.entities.other.CardIds
import ru.samtakoy.core.data.local.database.room.entities.other.CardWithTags

@Dao
interface CardDao {

    @Insert
    fun addCard(card: CardEntity): Long

    @Query("SELECT * FROM ${table} WHERE ${_id}=:id")
    fun getCard(id: Long): CardEntity?

    @Query("SELECT * FROM ${table} WHERE ${_id}=:id")
    fun getCardRx(id: Long): Flowable<CardEntity>

    @Query("SELECT $_id, $_qpack_id FROM ${table} WHERE ${_id}=:id")
    fun getCardIds(id: Long): CardIds

    @Query("SELECT * FROM ${table} WHERE ${_qpack_id}=:qPackId")
    fun getCardsFromQPack(qPackId: Long): Flowable<List<CardEntity>>

    @Query("SELECT $_id FROM ${table} WHERE ${_qpack_id}=:qPackId")
    fun getCardsIdsFromQPack(qPackId: Long): Single<List<Long>>


    @Transaction
    @Query("SELECT * FROM ${table} WHERE ${_qpack_id}=:qPackId")
    fun getCardsWithTagsFRomQPackRx(qPackId: Long): Single<List<CardWithTags>>


    @Update
    fun updateCard(card: CardEntity)

    @Query("DELETE FROM $table WHERE $_id = :cardId")
    fun deleteCard(cardId: Long)

    @Query("DELETE FROM $table WHERE $_qpack_id = :qPackId")
    fun deleteQPackCards(qPackId: Long): Int

    @Query("SELECT COUNT(*) FROM ${table} WHERE ${_qpack_id}=:qPackId")
    fun getCardCountInQPack(qPackId: Long): Int

}