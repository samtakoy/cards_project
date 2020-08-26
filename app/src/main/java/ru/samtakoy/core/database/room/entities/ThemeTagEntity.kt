package ru.samtakoy.core.database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = ThemeTagEntity.table,
        foreignKeys = [
            ForeignKey(
                    entity = ThemeEntity::class,
                    parentColumns = ["_id"],
                    childColumns = [ThemeTagEntity._theme_id],
                    onDelete = ForeignKey.RESTRICT
            ),
            ForeignKey(
                    entity = TagEntity::class,
                    parentColumns = ["_id"],
                    childColumns = [ThemeTagEntity._tag_id],
                    onDelete = ForeignKey.RESTRICT
            )
        ],
        primaryKeys = [ThemeTagEntity._theme_id, ThemeTagEntity._tag_id]
)
class ThemeTagEntity(
        @ColumnInfo(name = ThemeTagEntity._theme_id)
        var themeId: Long,
        @ColumnInfo(name = ThemeTagEntity._tag_id)
        var tagId: Long
) {

    companion object {
        const val table = "themes_tags"

        const val _theme_id = "theme_id"
        const val _tag_id = "tag_id"
    }
}