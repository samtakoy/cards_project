package ru.samtakoy.features.tag.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.samtakoy.features.tag.data.CardTagEntity.Companion._card_id
import ru.samtakoy.features.tag.data.CardTagEntity.Companion.table

@Dao
interface CardTagDao {

    /*
    @Query("SELECT * FROM ${CardTagEntity.table} WHERE ${CardTagEntity.id}=:id")
    fun getCard(id: Long): CardEntity*/

    @Query("DELETE FROM $table WHERE $_card_id = :cardId")
    fun deleteAllFromCard(cardId: Long)

    @Insert
    fun addTags(cardTags: List<CardTagEntity>)

}