package ru.samtakoy.data.qpack.mapper

import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.data.qpack.QPackEntity
import ru.samtakoy.domain.qpack.QPack
import kotlin.time.ExperimentalTime

internal interface QPackEntityMapper {
    fun mapToEntity(data: QPack): QPackEntity
    fun mapToDomain(data: QPackEntity): QPack
}

internal class QPackEntityMapperImpl() : QPackEntityMapper {
    override fun mapToEntity(data: QPack): QPackEntity {
        @OptIn(ExperimentalTime::class)
        return QPackEntity(
            id = data.id,
            themeId = data.themeId,
            path = data.path,
            fileName = data.fileName,
            title = data.title,
            desc = data.desc,
            creationDate = DateUtils.dateToDbSerialized(data.creationDate),
            viewCount = data.viewCount,
            lastViewDate = DateUtils.dateToDbSerialized(data.lastViewDate),
            favorite = data.favorite
        )
    }

    override fun mapToDomain(data: QPackEntity): QPack {
        @OptIn(ExperimentalTime::class)
        return QPack(
            id = data.id,
            themeId = data.themeId,
            path = data.path,
            fileName = data.fileName,
            title = data.title,
            desc = data.desc,
            creationDate = DateUtils.dateFromDbSerialized(data.creationDate),
            viewCount = data.viewCount,
            lastViewDate = DateUtils.dateFromDbSerialized(data.lastViewDate),
            favorite = data.favorite
        )
    }
}