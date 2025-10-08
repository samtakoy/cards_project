package ru.samtakoy.features.qpack.data.mapper

import ru.samtakoy.features.qpack.data.QPackEntity
import ru.samtakoy.features.qpack.domain.QPack
import javax.inject.Inject

internal interface QPackEntityMapper {
    fun mapToEntity(data: QPack): QPackEntity
    fun mapToDomain(data: QPackEntity): QPack
}

internal class QPackEntityMapperImpl @Inject constructor() : QPackEntityMapper {
    override fun mapToEntity(data: QPack): QPackEntity {
        return QPackEntity(
            id = data.id,
            themeId = data.themeId,
            path = data.path,
            fileName = data.fileName,
            title = data.title,
            desc = data.desc,
            creationDate = data.creationDate,
            viewCount = data.viewCount,
            lastViewDate = data.lastViewDate,
            favorite = data.favorite
        )
    }

    override fun mapToDomain(data: QPackEntity): QPack {
        return QPack(
            id = data.id,
            themeId = data.themeId,
            path = data.path,
            fileName = data.fileName,
            title = data.title,
            desc = data.desc,
            creationDate = data.creationDate,
            viewCount = data.viewCount,
            lastViewDate = data.lastViewDate,
            favorite = data.favorite
        )
    }
}