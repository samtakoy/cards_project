package ru.samtakoy.core.presentation.themes.mapper

import ru.samtakoy.common.utils.DateUtils.DATE_FORMAT
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.core.presentation.themes.ThemeUiItem
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.theme.Theme
import javax.inject.Inject

internal interface ThemeUiItemMapper {
    fun mapThemes(themes: List<Theme>): List<ThemeUiItem.Theme>
    fun mapQPacks(qPacks: List<QPack>): List<ThemeUiItem.QPack>
}

internal class ThemeUiItemMapperImpl @Inject constructor() : ThemeUiItemMapper {
    override fun mapThemes(themes: List<Theme>): List<ThemeUiItem.Theme> {
        return themes.map {
            ThemeUiItem.Theme(
                id = LongUiId(it.id),
                title = it.title.asAnnotated()
            )
        }
    }

    override fun mapQPacks(qPacks: List<QPack>): List<ThemeUiItem.QPack> {
        return qPacks.map {
            ThemeUiItem.QPack(
                id = LongUiId(it.id),
                title = it.title.asAnnotated(),
                creationDate = it.getCreationDateAsString().asAnnotated()
            )
        }
    }

    private fun QPack.getCreationDateAsString(): String {
        return DATE_FORMAT.format(creationDate)
    }
}