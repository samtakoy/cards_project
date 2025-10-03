package ru.samtakoy.core.data.local.database.room.dao

import androidx.room.*
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion._favorite
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion._id
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion._qpack_id
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion.table
import ru.samtakoy.core.data.local.database.room.entities.other.CardIds
import ru.samtakoy.core.data.local.database.room.entities.other.CardWithTags

@Dao
interface CardDao {

    @Insert
    fun addCardSync(card: CardEntity): Long

    @Insert
    suspend fun addCard(card: CardEntity): Long

    @Query("SELECT * FROM ${table} WHERE ${_id}=:id")
    suspend fun getCard(id: Long): CardEntity?

    @Query("SELECT * FROM ${table} WHERE ${_id}=:id")
    fun getCardAsFlow(id: Long): Flow<CardEntity?>

    @Query("SELECT $_id, $_qpack_id FROM ${table} WHERE ${_id}=:id")
    fun getCardIds(id: Long): CardIds

    @Query("SELECT * FROM ${table} WHERE ${_qpack_id}=:qPackId")
    suspend fun getCardsFromQPack(qPackId: Long): List<CardEntity>

    @Query("SELECT $_id FROM ${table} WHERE ${_qpack_id}=:qPackId")
    suspend fun getCardsIdsFromQPack(qPackId: Long): List<Long>

    @Transaction
    @Query("SELECT * FROM ${table} WHERE ${_qpack_id}=:qPackId")
    suspend fun getCardsWithTagsFRomQPack(qPackId: Long): List<CardWithTags>

    @Update
    fun updateCardSync(card: CardEntity)

    @Update
    suspend fun updateCard(card: CardEntity)

    @Query("DELETE FROM $table WHERE $_id = :cardId")
    fun deleteCard(cardId: Long)

    @Query("DELETE FROM $table WHERE $_qpack_id = :qPackId")
    suspend fun deleteQPackCards(qPackId: Long): Int

    @Query("SELECT COUNT(*) FROM ${table} WHERE ${_qpack_id}=:qPackId")
    fun getCardCountInQPack(qPackId: Long): Int

    // Favorites
    @Query("SELECT $_id FROM ${table} WHERE ${_favorite}>0")
    fun getAllFavoriteCardsIdsRx(): Single<List<Long>>

    @Query("SELECT $_id FROM ${table} WHERE ${_favorite}>0")
    suspend fun getAllFavoriteCardsIds(): List<Long>

    @Query("SELECT $_id FROM ${table} WHERE ${_favorite}>0 AND $_qpack_id in (:qPackIds)")
    suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long>

    @Query("SELECT count(*) FROM ${table} WHERE ${_favorite}>0")
    suspend fun getFavoriteCardsCount(): Int

    @Query("UPDATE $table SET ${_favorite}=:favorite WHERE ${_id}=:id")
    suspend fun updateFavorite(id: Long, favorite: Int)
}