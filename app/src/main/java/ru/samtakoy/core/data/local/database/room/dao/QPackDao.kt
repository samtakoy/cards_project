package ru.samtakoy.core.data.local.database.room.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import ru.samtakoy.core.data.local.database.room.converters.DateLongConverter
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._creation_date
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._id
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._last_view_date
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._theme_id
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._view_counter
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion.table
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds

@Dao
interface QPackDao {

    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getQPack(id: Long): Single<QPackEntity>

    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getQPackRx(id: Long): Flowable<QPackEntity>

    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getQPackWithCardIds(id: Long): Flowable<QPackWithCardIds>

    @Query("SELECT * FROM $table WHERE ${_theme_id}=:themeId")
    fun getQPacksFromTheme(themeId: Long): List<QPackEntity>

    @Query("SELECT * FROM $table WHERE ${_theme_id}=:themeId")
    fun getQPacksFromThemeRx(themeId: Long): Flowable<List<QPackEntity>>

    @Query("SELECT * FROM $table")
    fun getAllQPacks(): Single<List<QPackEntity>>


    @Query("SELECT * FROM $table ORDER BY $_last_view_date ")
    fun getAllQPacksByLastViewDateAsc(): Flowable<List<QPackEntity>>

    @Query("SELECT * FROM $table ORDER BY $_creation_date DESC")
    fun getAllQPacksByCreationDateDesc(): Flowable<List<QPackEntity>>


    @Insert
    fun addQPack(qPack: QPackEntity): Long

    @Update
    fun updateQPack(qPack: QPackEntity)

    @Query("UPDATE $table SET ${_view_counter}=${_view_counter}+1, $_last_view_date=:currentTime WHERE ${_id}=:qPackId")
    @TypeConverters(DateLongConverter::class)
    fun updateQPackViewCount(qPackId: Long, currentTime: java.util.Date)

    @Query("DELETE FROM $table WHERE ${_id}=:id")
    fun deleteQPackById(id: Long): Int

    @Query("SELECT COUNT(*) FROM $table WHERE $_id = :id")
    fun isPackExists(id: Long): Int

    @Query("SELECT COUNT(*) FROM $table ")
    fun getAllQPackCount(): Int
}