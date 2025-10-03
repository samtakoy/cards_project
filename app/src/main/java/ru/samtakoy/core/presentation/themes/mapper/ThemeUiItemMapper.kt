package ru.samtakoy.core.presentation.themes.mapper

import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.core.presentation.themes.ThemeUiItem
import javax.inject.Inject

internal interface ThemeUiItemMapper {
    fun mapThemes(themes: List<ThemeEntity>): List<ThemeUiItem.Theme>
    fun mapQPacks(qPacks: List<QPackEntity>): List<ThemeUiItem.QPack>
}

internal class ThemeUiItemMapperImpl @Inject constructor() : ThemeUiItemMapper {
    override fun mapThemes(themes: List<ThemeEntity>): List<ThemeUiItem.Theme> {
        return themes.map {
            ThemeUiItem.Theme(
                id = LongUiId(it.id),
                title = it.title.asAnnotated()
            )
        }
    }

    override fun mapQPacks(qPacks: List<QPackEntity>): List<ThemeUiItem.QPack> {
        return qPacks.map {
            ThemeUiItem.QPack(
                id = LongUiId(it.id),
                title = it.title.asAnnotated(),
                creationDate = it.getCreationDateAsString().asAnnotated()
            )
        }
    }
}