package ru.samtakoy.core.data.local.database.room.dao

import androidx.room.*
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.converters.DateLongConverter
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._creation_date
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._favorite
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._id
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._last_view_date
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._theme_id
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._title
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion._view_counter
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity.Companion.table
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds

@Dao
interface QPackDao {

    @Query("SELECT * FROM $table WHERE $_id=:id")
    suspend fun getQPack(id: Long): QPackEntity?

    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getQPackRx(id: Long): Flowable<QPackEntity>

    @Transaction
    @Query("SELECT * FROM $table WHERE $_id=:id")
    fun getQPackWithCardIdsAsFlow(id: Long): Flow<QPackWithCardIds>

    @Query("SELECT * FROM $table WHERE ${_theme_id}=:themeId")
    fun getQPacksFromTheme(themeId: Long): List<QPackEntity>

    @Query("SELECT * FROM $table WHERE ${_theme_id}=:themeId")
    fun getQPacksFromThemeAsFlow(themeId: Long): Flow<List<QPackEntity>>

    @Query("SELECT * FROM $table")
    suspend fun getAllQPacks(): List<QPackEntity>


    @Query("SELECT * FROM $table ORDER BY $_last_view_date")
    fun getAllQPacksByLastViewDateAscAsFlow(): Flow<List<QPackEntity>>

    @Query("SELECT * FROM $table WHERE $_favorite>0 ORDER BY $_last_view_date")
    fun getAllFavQPacksByLastViewDateAscAsFlow(): Flow<List<QPackEntity>>

    @Query("SELECT * FROM $table WHERE $_title LIKE :searchString ORDER BY $_last_view_date")
    fun getAllQPacksByLastViewDateAscFilteredAsFlow(searchString: String): Flow<List<QPackEntity>>

    @Query("SELECT * FROM $table WHERE $_title LIKE :searchString AND $_favorite>0 ORDER BY $_last_view_date")
    fun getAllFavQPacksByLastViewDateAscFilteredAsFlow(searchString: String): Flow<List<QPackEntity>>

    @Query("SELECT * FROM $table ORDER BY $_creation_date DESC")
    fun getAllQPacksByCreationDateDescAsFlow(): Flow<List<QPackEntity>>

    @Query("SELECT * FROM $table WHERE $_favorite>0 ORDER BY $_creation_date DESC")
    fun getAllFavQPacksByCreationDateDescAsFlow(): Flow<List<QPackEntity>>

    @Query("SELECT * FROM $table WHERE $_title LIKE :searchString ORDER BY $_creation_date DESC")
    fun getAllQPacksByCreationDateDescFilteredAsFlow(searchString: String): Flow<List<QPackEntity>>

    @Query("SELECT * FROM $table WHERE $_title LIKE :searchString AND $_favorite>0 ORDER BY $_creation_date DESC")
    fun getAllFavQPacksByCreationDateDescFilteredAsFlow(searchString: String): Flow<List<QPackEntity>>

    @Query(
        """SELECT $_id FROM $table
           WHERE $_id in
                (SELECT DISTINCT ${CardEntity._qpack_id}
                    FROM ${CardEntity.table}
                    WHERE ${CardEntity._favorite}>0
                )
           ORDER BY $_creation_date DESC
           """
    )
    suspend fun getAllQPacksIdsByCreationDateDescWithFavs(): List<Long>

    @Query(
        """SELECT $_id FROM $table
           WHERE $_id in
                (SELECT DISTINCT ${CardEntity._qpack_id}
                    FROM ${CardEntity.table}
                    WHERE ${CardEntity._favorite}>0
                )
           ORDER BY $_creation_date DESC
           """
    )
    fun getAllQPacksIdsByCreationDateDescWithFavsAsFlow(): Flow<List<Long>>

    @Query("SELECT * FROM $table WHERE $_id in (:ids)")
    suspend fun getQPacksByIds(ids: List<Long>): List<QPackEntity>

    @Insert
    fun addQPack(qPack: QPackEntity): Long

    @Update
    fun updateQPack(qPack: QPackEntity)

    @Query("UPDATE $table SET ${_view_counter}=${_view_counter}+1, $_last_view_date=:currentTime WHERE ${_id}=:qPackId")
    @TypeConverters(DateLongConverter::class)
    suspend fun updateQPackViewCount(qPackId: Long, currentTime: java.util.Date)

    @Query("UPDATE $table SET ${_favorite}=:favorite WHERE ${_id}=:qPackId")
    fun updateQPackFavorite(qPackId: Long, favorite: Int)

    @Query("DELETE FROM $table WHERE ${_id}=:id")
    suspend fun deleteQPackById(id: Long): Int

    @Query("SELECT COUNT(*) FROM $table WHERE $_id = :id")
    fun isPackExists(id: Long): Int

    @Query("SELECT COUNT(*) FROM $table ")
    fun getAllQPackCount(): Int
}