package ru.samtakoy.data.cardtag

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.samtakoy.data.cardtag.model.TagEntity

@Dao
internal interface TagDao {

    @Insert
    suspend fun addTag(tag: TagEntity): Long

    @Insert
    suspend fun addTags(tags: List<TagEntity>): List<Long>

    @Query("SELECT * FROM ${TagEntity.table} WHERE ${TagEntity._id}=:id")
    suspend fun getTag(id: Long): TagEntity

    @Query("SELECT * FROM ${TagEntity.table}")
    suspend fun getAllTags(): List<TagEntity>

    @Query("SELECT * FROM ${TagEntity.table} WHERE ${TagEntity._id} IN (:ids)")
    suspend fun getAllById(ids: List<Long>): List<TagEntity>
}