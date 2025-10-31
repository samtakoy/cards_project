package ru.samtakoy.data.cardtag

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.samtakoy.data.cardtag.model.TagEntity

@Dao
internal interface TagDao {

    @Insert
    fun addTag(tag: TagEntity): Long

    @Insert
    fun addTags(tags: List<TagEntity>): List<Long>

    @Query("SELECT * FROM ${TagEntity.table} WHERE ${TagEntity._id}=:id")
    fun getTag(id: Long): TagEntity

    @Query("SELECT * FROM ${TagEntity.table}")
    fun getAllTags(): List<TagEntity>

    @Query("SELECT * FROM ${TagEntity.table} WHERE ${TagEntity._id} IN (:ids)")
    fun getAllById(ids: List<Long>): List<TagEntity>
}