package ru.samtakoy.features.views.data.local.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity
import ru.samtakoy.features.views.data.local.model.ViewHistoryEntity.Companion.ID
import ru.samtakoy.features.views.data.local.model.ViewHistoryEntity.Companion.LAST_VIEW_DATE
import ru.samtakoy.features.views.data.local.model.ViewHistoryEntity.Companion.QPACK_ID
import ru.samtakoy.features.views.data.local.model.ViewHistoryEntity.Companion.TABLE
import ru.samtakoy.features.views.domain.ViewHistoryItem

@Dao
interface ViewHistoryDao {
    /** добавить просмотр */
    @Insert
    suspend fun add(item: ViewHistoryEntity): Long

    /** изменить просмотр */
    @Update
    suspend fun update(item: ViewHistoryEntity): Int

    /** запросить последний */
    @Query(
        """SELECT * FROM $TABLE
            WHERE $QPACK_ID = :qPackId
            ORDER BY $LAST_VIEW_DATE DESC
            LIMIT 1"""
    )
    fun getLastViewFor(qPackId: Long): Flow<ViewHistoryEntity?>

    @Query(
        """SELECT * FROM $TABLE
            WHERE $ID = :viewItemId"""
    )
    fun getViewHistoryItemAsFlow(viewItemId: Long): Flow<ViewHistoryEntity?>

    @Query(
        """SELECT * FROM $TABLE
            ORDER BY $LAST_VIEW_DATE DESC"""
    )
    fun getAllViewHistoryItemsDescByTime(): Flow<List<ViewHistoryEntity>>


    @Query(
        """SELECT * FROM $TABLE
            WHERE $QPACK_ID = :qPackId
            ORDER BY $LAST_VIEW_DATE DESC
            LIMIT 1"""
    )
    fun getLastViewHistoryItemFor(qPackId: Long): Flow<ViewHistoryEntity?>

    @Query("""SELECT * FROM $TABLE WHERE $ID = :viewId""")
    suspend fun getViewHistoryItemById(viewId: Long): ViewHistoryEntity?

    @Query(
        """SELECT
                $TABLE.*,
                ${QPackEntity.table}.${QPackEntity._title} AS ${ViewHistoryEntityWithTheme.QPACK_TITLE}, 
                ${ThemeEntity.table}.${ThemeEntity._title} AS ${ViewHistoryEntityWithTheme.THEME_TITLE} 
            FROM $TABLE
            LEFT JOIN ${QPackEntity.table}
                ON $TABLE.$QPACK_ID = ${QPackEntity.table}.${QPackEntity._id} 
            LEFT JOIN ${ThemeEntity.table}
                ON ${QPackEntity.table}.${QPackEntity._theme_id} = ${ThemeEntity.table}.${ThemeEntity._id}
            ORDER BY $TABLE.$LAST_VIEW_DATE DESC"""
    )
    fun getAllViewHistoryItemsWithThemeTitleDescByTime(): Flow<List<ViewHistoryEntityWithTheme>>

    /** удалить просмотр */
    @Query("DELETE FROM $TABLE WHERE ${ViewHistoryEntity.ID}=:id")
    suspend fun delete(id: Long): Int
}