package ru.samtakoy.data.view.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.data.qpack.QPackEntity
import ru.samtakoy.data.theme.ThemeEntity
import ru.samtakoy.data.view.model.ViewHistoryEntity.Companion.ID

@Dao
internal interface ViewHistoryDao {
    /** добавить просмотр */
    @Insert
    suspend fun add(item: ViewHistoryEntity): Long

    /** изменить просмотр */
    @Update
    suspend fun update(item: ViewHistoryEntity): Int

    /** запросить последний */
    @Query(
        """SELECT * FROM ${ViewHistoryEntity.Companion.TABLE}
            WHERE ${ViewHistoryEntity.Companion.QPACK_ID} = :qPackId
            ORDER BY ${ViewHistoryEntity.Companion.LAST_VIEW_DATE} DESC
            LIMIT 1"""
    )
    fun getLastViewFor(qPackId: Long): Flow<ViewHistoryEntity?>

    @Query(
        """SELECT * FROM ${ViewHistoryEntity.Companion.TABLE}
            WHERE ${ViewHistoryEntity.Companion.ID} = :viewItemId"""
    )
    fun getViewHistoryItemAsFlow(viewItemId: Long): Flow<ViewHistoryEntity?>

    @Query(
        """SELECT * FROM ${ViewHistoryEntity.Companion.TABLE}
            ORDER BY ${ViewHistoryEntity.Companion.LAST_VIEW_DATE} DESC"""
    )
    fun getAllViewHistoryItemsDescByTime(): Flow<List<ViewHistoryEntity>>


    @Query(
        """SELECT * FROM ${ViewHistoryEntity.Companion.TABLE}
            WHERE ${ViewHistoryEntity.Companion.QPACK_ID} = :qPackId
            ORDER BY ${ViewHistoryEntity.Companion.LAST_VIEW_DATE} DESC
            LIMIT 1"""
    )
    fun getLastViewHistoryItemFor(qPackId: Long): Flow<ViewHistoryEntity?>

    @Query("""SELECT * FROM ${ViewHistoryEntity.Companion.TABLE} WHERE ${ViewHistoryEntity.Companion.ID} = :viewId""")
    suspend fun getViewHistoryItemById(viewId: Long): ViewHistoryEntity?

    @Query(
        """SELECT
                ${ViewHistoryEntity.Companion.TABLE}.*,
                ${QPackEntity.table}.${QPackEntity._title} AS ${ViewHistoryEntityWithTheme.QPACK_TITLE}, 
                ${ThemeEntity.table}.${ThemeEntity._title} AS ${ViewHistoryEntityWithTheme.THEME_TITLE} 
            FROM ${ViewHistoryEntity.Companion.TABLE}
            LEFT JOIN ${QPackEntity.table}
                ON ${ViewHistoryEntity.Companion.TABLE}.${ViewHistoryEntity.Companion.QPACK_ID} = ${QPackEntity.table}.${QPackEntity._id} 
            LEFT JOIN ${ThemeEntity.table}
                ON ${QPackEntity.table}.${QPackEntity._theme_id} = ${ThemeEntity.table}.${ThemeEntity._id}
            ORDER BY ${ViewHistoryEntity.Companion.TABLE}.${ViewHistoryEntity.Companion.LAST_VIEW_DATE} DESC"""
    )
    fun getAllViewHistoryItemsWithThemeTitleDescByTime(): Flow<List<ViewHistoryEntityWithTheme>>

    /** удалить просмотр */
    @Query("DELETE FROM ${ViewHistoryEntity.Companion.TABLE} WHERE ${ID}=:id")
    suspend fun delete(id: Long): Int
}