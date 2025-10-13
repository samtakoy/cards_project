package ru.samtakoy.data.card

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.data.card.model.CardEntity
import ru.samtakoy.data.card.model.CardWithTagsEntity

@Dao
internal interface CardDao {

    @Insert
    fun addCardSync(card: CardEntity): Long

    @Insert
    suspend fun addCard(card: CardEntity): Long

    @Query("SELECT * FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._id}=:id")
    suspend fun getCard(id: Long): CardEntity?

    @Query("SELECT * FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._id}=:id")
    fun getCardAsFlow(id: Long): Flow<CardEntity?>

    @Query("SELECT ${CardEntity.Companion._qpack_id} FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._id}=:id")
    fun getCardQPackId(id: Long): Long?

    @Query("SELECT * FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._qpack_id}=:qPackId")
    suspend fun getCardsFromQPack(qPackId: Long): List<CardEntity>

    @Query("SELECT ${CardEntity.Companion._id} FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._qpack_id}=:qPackId")
    suspend fun getCardsIdsFromQPack(qPackId: Long): List<Long>

    @Query("SELECT ${CardEntity.Companion._id} FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._qpack_id}=:qPackId")
    fun getCardsIdsFromQPackAsFlow(qPackId: Long): Flow<List<Long>>

    @Transaction
    @Query("SELECT * FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._qpack_id}=:qPackId")
    suspend fun getCardsWithTagsFromQPack(qPackId: Long): List<CardWithTagsEntity>

    @Update
    fun updateCardSync(card: CardEntity)

    @Update
    suspend fun updateCard(card: CardEntity)

    @Query("DELETE FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._id} = :cardId")
    fun deleteCard(cardId: Long)

    @Query("DELETE FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._qpack_id} = :qPackId")
    suspend fun deleteQPackCards(qPackId: Long): Int

    @Query("SELECT COUNT(*) FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._qpack_id}=:qPackId")
    fun getCardCountInQPack(qPackId: Long): Int

    // Favorites
    @Query("SELECT ${CardEntity.Companion._id} FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._favorite}>0")
    suspend fun getAllFavoriteCardsIds(): List<Long>

    @Query("SELECT ${CardEntity.Companion._id} FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._favorite}>0 AND ${CardEntity.Companion._qpack_id} in (:qPackIds)")
    suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long>

    @Query("SELECT count(*) FROM ${CardEntity.Companion.table} WHERE ${CardEntity.Companion._favorite}>0")
    suspend fun getFavoriteCardsCount(): Int

    @Query("UPDATE ${CardEntity.Companion.table} SET ${CardEntity.Companion._favorite}=:favorite WHERE ${CardEntity.Companion._id}=:id")
    suspend fun updateFavorite(id: Long, favorite: Int)
}