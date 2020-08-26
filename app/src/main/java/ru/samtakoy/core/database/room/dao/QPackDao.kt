package ru.samtakoy.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Flowable
import ru.samtakoy.core.database.room.entities.QPackEntity
import ru.samtakoy.core.database.room.entities.QPackEntity.Companion._creation_date
import ru.samtakoy.core.database.room.entities.QPackEntity.Companion._id
import ru.samtakoy.core.database.room.entities.QPackEntity.Companion._last_view_date
import ru.samtakoy.core.database.room.entities.QPackEntity.Companion._theme_id
import ru.samtakoy.core.database.room.entities.QPackEntity.Companion.table

@Dao
interface QPackDao {

    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getQPack(id: Long): QPackEntity

    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getQPackRx(id: Long): Flowable<QPackEntity>

    @Query("SELECT * FROM $table WHERE ${_theme_id}=:themeId")
    fun getQPacksFromTheme(themeId: Long): List<QPackEntity>

    @Query("SELECT * FROM $table WHERE ${_theme_id}=:themeId")
    fun getQPacksFromThemeRx(themeId: Long): Flowable<List<QPackEntity>>

    @Query("SELECT * FROM $table")
    fun getAllQPacks(): Flowable<List<QPackEntity>>


    @Query("SELECT * FROM $table ORDER BY $_last_view_date ")
    fun getAllQPacksByLastViewDateAsc(): Flowable<List<QPackEntity>>

    @Query("SELECT * FROM $table ORDER BY $_creation_date DESC")
    fun getAllQPacksByCreationDateDesc(): Flowable<List<QPackEntity>>


    @Insert
    fun addQPack(qPack: QPackEntity): Long

    @Update
    fun updateQPack(qPack: QPackEntity)

    @Query("DELETE FROM $table WHERE ${_id}=:id")
    fun deleteQPackById(id: Long): Int

    @Query("SELECT COUNT(*) FROM $table WHERE $_id = :id")
    fun isPackExists(id: Long): Int

    @Query("SELECT COUNT(*) FROM $table ")
    fun getAllQPackCount(): Int
}