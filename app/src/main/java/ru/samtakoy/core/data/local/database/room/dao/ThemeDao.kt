package ru.samtakoy.core.data.local.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity.Companion._id
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity.Companion._parent
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity.Companion._title
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity.Companion.table

@Dao
interface ThemeDao {

    @Query("SELECT * FROM ${table} WHERE $_id=:id")
    suspend fun getTheme(id: Long): ThemeEntity?

    @Query("SELECT * FROM ${table} WHERE $_id=:id")
    fun getThemeSingle(id: Long): Single<ThemeEntity>

    @Query("SELECT * FROM ${table} WHERE $_parent = :parentThemeId AND $_title = :title")
    fun getThemeWithTitle(parentThemeId: Long, title: String): ThemeEntity?

    @Query("SELECT * FROM $table WHERE ${ThemeEntity._parent}=:parentId")
    fun getChildThemes(parentId: Long): List<ThemeEntity>

    @Query("SELECT * FROM $table WHERE ${ThemeEntity._parent}=:parentId")
    fun getChildThemesAsFlow(parentId: Long): Flow<List<ThemeEntity>>

    @Insert
    fun addTheme(theme: ThemeEntity): Long

    @Query("DELETE FROM $table WHERE $_id=:id")
    fun deleteThemeById(id: Long): Int


}