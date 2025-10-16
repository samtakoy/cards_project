package ru.samtakoy.data.theme.mapper

import ru.samtakoy.data.theme.ThemeEntity
import ru.samtakoy.domain.theme.Theme

internal interface ThemeEntityMapper {
    fun mapToEnity(data: Theme): ThemeEntity
    fun mapToDomain(data: ThemeEntity): Theme
}

internal class ThemeEntityMapperImpl() : ThemeEntityMapper {
    override fun mapToEnity(data: Theme): ThemeEntity {
        return ThemeEntity(
            id = data.id,
            title = data.title,
            parentId = data.parentId
        )
    }

    override fun mapToDomain(data: ThemeEntity): Theme {
        return Theme(
            id = data.id,
            title = data.title,
            parentId = data.parentId
        )
    }
}