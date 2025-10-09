package ru.samtakoy.features.tag.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TagDao {

    @Insert
    fun addTag(tag: TagEntity): Long

    @Query("SELECT * FROM ${TagEntity.table} WHERE ${TagEntity._id}=:id")
    fun getTag(id: Long): TagEntity

    @Query("SELECT * FROM ${TagEntity.table} ")
    fun getAllTags(): List<TagEntity>


}