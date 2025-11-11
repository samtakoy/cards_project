package ru.samtakoy.data.theme

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.data.theme.ThemeEntity.Companion._parent

@Dao
internal interface ThemeDao {

    @Query("SELECT * FROM ${ThemeEntity.Companion.table} WHERE ${ThemeEntity.Companion._id}=:id")
    suspend fun getTheme(id: Long): ThemeEntity?

    @Query("SELECT * FROM ${ThemeEntity.Companion.table} WHERE ${_parent} = :parentThemeId AND ${ThemeEntity.Companion._title} = :title")
    fun getThemeWithTitle(parentThemeId: Long, title: String): ThemeEntity?

    @Query("SELECT * FROM ${ThemeEntity.Companion.table} WHERE ${ThemeEntity.Companion._parent}=:parentId")
    suspend fun getChildThemes(parentId: Long): List<ThemeEntity>

    @Query("SELECT * FROM ${ThemeEntity.Companion.table} WHERE ${ThemeEntity.Companion._parent}=:parentId")
    fun getChildThemesAsFlow(parentId: Long): Flow<List<ThemeEntity>>

    @Insert
    suspend fun addTheme(theme: ThemeEntity): Long

    @Query("DELETE FROM ${ThemeEntity.Companion.table} WHERE ${ThemeEntity.Companion._id}=:id")
    suspend fun deleteThemeById(id: Long): Int
}