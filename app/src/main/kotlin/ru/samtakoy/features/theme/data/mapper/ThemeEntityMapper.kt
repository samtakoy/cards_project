package ru.samtakoy.features.theme.data.mapper

import ru.samtakoy.features.theme.data.ThemeEntity
import ru.samtakoy.features.theme.domain.Theme
import javax.inject.Inject

internal interface ThemeEntityMapper {
    fun mapToEnity(data: Theme): ThemeEntity
    fun mapToDomain(data: ThemeEntity): Theme
}

internal class ThemeEntityMapperImpl @Inject constructor() : ThemeEntityMapper {
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