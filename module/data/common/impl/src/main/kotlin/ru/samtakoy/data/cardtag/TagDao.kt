package ru.samtakoy.data.cardtag

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.samtakoy.data.cardtag.model.TagEntity

@Dao
internal interface TagDao {

    @Insert
    fun addTag(tag: TagEntity): Long

    @Query("SELECT * FROM ${TagEntity.table} WHERE ${TagEntity._id}=:id")
    fun getTag(id: Long): TagEntity

    @Query("SELECT * FROM ${TagEntity.table} ")
    fun getAllTags(): List<TagEntity>


}