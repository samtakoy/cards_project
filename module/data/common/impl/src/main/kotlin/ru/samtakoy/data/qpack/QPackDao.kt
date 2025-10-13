package ru.samtakoy.data.qpack

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.data.common.db.converters.DateLongConverter
import ru.samtakoy.data.card.model.CardEntity
import java.util.Date

@Dao
internal interface QPackDao {

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._id}=:id")
    suspend fun getQPack(id: Long): QPackEntity?

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._id}=:id")
    fun getQPackAsFlow(id: Long): Flow<QPackEntity>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._theme_id}=:themeId")
    fun getQPacksFromTheme(themeId: Long): List<QPackEntity>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._theme_id}=:themeId")
    fun getQPacksFromThemeAsFlow(themeId: Long): Flow<List<QPackEntity>>

    @Query("SELECT * FROM ${QPackEntity.Companion.table}")
    suspend fun getAllQPacks(): List<QPackEntity>


    @Query("SELECT * FROM ${QPackEntity.Companion.table} ORDER BY ${QPackEntity.Companion._last_view_date}")
    fun getAllQPacksByLastViewDateAscAsFlow(): Flow<List<QPackEntity>>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._favorite}>0 ORDER BY ${QPackEntity.Companion._last_view_date}")
    fun getAllFavQPacksByLastViewDateAscAsFlow(): Flow<List<QPackEntity>>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._title} LIKE :searchString ORDER BY ${QPackEntity.Companion._last_view_date}")
    fun getAllQPacksByLastViewDateAscFilteredAsFlow(searchString: String): Flow<List<QPackEntity>>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._title} LIKE :searchString AND ${QPackEntity.Companion._favorite}>0 ORDER BY ${QPackEntity.Companion._last_view_date}")
    fun getAllFavQPacksByLastViewDateAscFilteredAsFlow(searchString: String): Flow<List<QPackEntity>>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} ORDER BY ${QPackEntity.Companion._creation_date} DESC")
    fun getAllQPacksByCreationDateDescAsFlow(): Flow<List<QPackEntity>>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._favorite}>0 ORDER BY ${QPackEntity.Companion._creation_date} DESC")
    fun getAllFavQPacksByCreationDateDescAsFlow(): Flow<List<QPackEntity>>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._title} LIKE :searchString ORDER BY ${QPackEntity.Companion._creation_date} DESC")
    fun getAllQPacksByCreationDateDescFilteredAsFlow(searchString: String): Flow<List<QPackEntity>>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._title} LIKE :searchString AND ${QPackEntity.Companion._favorite}>0 ORDER BY ${QPackEntity.Companion._creation_date} DESC")
    fun getAllFavQPacksByCreationDateDescFilteredAsFlow(searchString: String): Flow<List<QPackEntity>>

    @Query(
        """SELECT ${QPackEntity.Companion._id} FROM ${QPackEntity.Companion.table}
           WHERE ${QPackEntity.Companion._id} in
                (SELECT DISTINCT ${CardEntity._qpack_id}
                    FROM ${CardEntity.table}
                    WHERE ${CardEntity._favorite}>0
                )
           ORDER BY ${QPackEntity.Companion._creation_date} DESC
           """
    )
    suspend fun getAllQPacksIdsByCreationDateDescWithFavs(): List<Long>

    @Query(
        """SELECT ${QPackEntity.Companion._id} FROM ${QPackEntity.Companion.table}
           WHERE ${QPackEntity.Companion._id} in
                (SELECT DISTINCT ${CardEntity._qpack_id}
                    FROM ${CardEntity.table}
                    WHERE ${CardEntity._favorite}>0
                )
           ORDER BY ${QPackEntity.Companion._creation_date} DESC
           """
    )
    fun getAllQPacksIdsByCreationDateDescWithFavsAsFlow(): Flow<List<Long>>

    @Query("SELECT * FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._id} in (:ids)")
    suspend fun getQPacksByIds(ids: List<Long>): List<QPackEntity>

    @Insert
    fun addQPack(qPack: QPackEntity): Long

    @Update
    fun updateQPack(qPack: QPackEntity)

    @Query("UPDATE ${QPackEntity.Companion.table} SET ${QPackEntity.Companion._view_counter}=${QPackEntity.Companion._view_counter}+1, ${QPackEntity.Companion._last_view_date}=:currentTime WHERE ${QPackEntity.Companion._id}=:qPackId")
    @TypeConverters(DateLongConverter::class)
    suspend fun updateQPackViewCount(qPackId: Long, currentTime: Date)

    @Query("UPDATE ${QPackEntity.Companion.table} SET ${QPackEntity.Companion._favorite}=:favorite WHERE ${QPackEntity.Companion._id}=:qPackId")
    fun updateQPackFavorite(qPackId: Long, favorite: Int)

    @Query("DELETE FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._id}=:id")
    suspend fun deleteQPackById(id: Long): Int

    @Query("SELECT COUNT(*) FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity.Companion._id} = :id")
    fun isPackExists(id: Long): Int

    @Query("SELECT COUNT(*) FROM ${QPackEntity.Companion.table} ")
    fun getAllQPackCount(): Int

    @Query("SELECT COUNT(*) FROM ${QPackEntity.Companion.table} WHERE ${QPackEntity._theme_id} = :themeId")
    suspend fun getQPacksFromThemeCount(themeId: Long): Int
}