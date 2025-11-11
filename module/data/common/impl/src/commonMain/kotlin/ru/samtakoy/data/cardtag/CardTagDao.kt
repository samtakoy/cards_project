package ru.samtakoy.data.cardtag

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.samtakoy.data.cardtag.model.CardTagEntity
import ru.samtakoy.data.cardtag.model.CardTagEntity.Companion._card_id
import ru.samtakoy.data.cardtag.model.CardTagEntity.Companion.table

@Dao
internal interface CardTagDao {

    @Query("DELETE FROM $table WHERE $_card_id = :cardId")
    suspend fun deleteAllFromCard(cardId: Long)

    @Insert
    suspend fun addTags(cardTags: List<CardTagEntity>)

}