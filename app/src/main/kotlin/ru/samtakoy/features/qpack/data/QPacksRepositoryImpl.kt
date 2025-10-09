package ru.samtakoy.features.qpack.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.samtakoy.features.qpack.data.mapper.QPackEntityMapper
import ru.samtakoy.features.qpack.domain.QPack
import java.util.Date
import javax.inject.Inject

internal class QPacksRepositoryImpl @Inject constructor(
    private val qPackDao: QPackDao,
    private val qPackMapper: QPackEntityMapper
) : QPacksRepository {

    override fun addQPack(qPack: QPack): Long {
        return qPackDao.addQPack(qPackMapper.mapToEntity(qPack))
    }

    override suspend fun getQPack(qPackId: Long): QPack? {
        return qPackDao.getQPack(qPackId)?.let(qPackMapper::mapToDomain)
    }

    override fun getQPackAsFlow(qPackId: Long): Flow<QPack> {
        return qPackDao.getQPackAsFlow(qPackId).map(qPackMapper::mapToDomain)
    }

    override suspend fun deletePack(qPackId: Long) {
        qPackDao.deleteQPackById(qPackId)
    }

    override fun updateQPack(qPack: QPack) {
        qPackDao.updateQPack(qPackMapper.mapToEntity(qPack))
    }

    override fun updateQPackFavorite(qPackId: Long, favorite: Int) {
        qPackDao.updateQPackFavorite(qPackId, favorite)
    }

    override suspend fun updateQPackViewCount(qPackId: Long, currentTime: Date) {
        qPackDao.updateQPackViewCount(qPackId, currentTime)
    }

    override fun isPackExists(qPackId: Long): Boolean {
        return qPackDao.isPackExists(qPackId) > 0
    }

    override fun getQPacksFromTheme(themeId: Long): List<QPack> {
        return qPackDao.getQPacksFromTheme(themeId).map(qPackMapper::mapToDomain)
    }

    override fun getQPacksFromThemeAsFlow(themeId: Long): Flow<List<QPack>> {
        return qPackDao.getQPacksFromThemeAsFlow(themeId).map { it.map(qPackMapper::mapToDomain) }
    }

    override suspend fun getAllQPacks(): List<QPack> {
        return qPackDao.getAllQPacks().map(qPackMapper::mapToDomain)
    }

    override fun getAllQPacksByLastViewDateAscAsFlow(onlyFavorites: Boolean): Flow<List<QPack>> {
        return if (onlyFavorites) {
            qPackDao.getAllFavQPacksByLastViewDateAscAsFlow()
        } else {
            qPackDao.getAllQPacksByLastViewDateAscAsFlow()
        }.map {
            it.map(qPackMapper::mapToDomain)
        }
    }

    override fun getAllQPacksByLastViewDateAscFilteredAsFlow(
        searchString: String,
        onlyFavorites: Boolean
    ): Flow<List<QPack>> {
        return if (onlyFavorites) {
            qPackDao.getAllFavQPacksByLastViewDateAscFilteredAsFlow("%$searchString%")
        } else {
            qPackDao.getAllQPacksByLastViewDateAscFilteredAsFlow("%$searchString%")
        }.map {
            it.map(qPackMapper::mapToDomain)
        }
    }

    override fun getAllQPacksByCreationDateDescAsFlow(onlyFavorites: Boolean): Flow<List<QPack>> {
        return if (onlyFavorites) {
            qPackDao.getAllFavQPacksByCreationDateDescAsFlow()
        } else {
            qPackDao.getAllQPacksByCreationDateDescAsFlow()
        }.map {
            it.map(qPackMapper::mapToDomain)
        }
    }

    override fun getAllQPacksByCreationDateDescFilteredAsFlow(
        searchString: String,
        onlyFavorites: Boolean
    ): Flow<List<QPack>> {
        return if (onlyFavorites) {
            qPackDao.getAllFavQPacksByCreationDateDescFilteredAsFlow("%$searchString%")
        } else {
            qPackDao.getAllQPacksByCreationDateDescFilteredAsFlow("%$searchString%")
        }.map {
            it.map(qPackMapper::mapToDomain)
        }
    }

    override suspend fun getAllQPacksIdsByCreationDateDescWithFavs(): List<Long> {
        return qPackDao.getAllQPacksIdsByCreationDateDescWithFavs()
    }

    override fun getAllQPacksIdsByCreationDateDescWithFavsAsFlow(): Flow<List<Long>> {
        return qPackDao.getAllQPacksIdsByCreationDateDescWithFavsAsFlow().distinctUntilChanged()
    }

    override suspend fun getQPacksByIds(ids: List<Long>): List<QPack> {
        return qPackDao.getQPacksByIds(ids).map(qPackMapper::mapToDomain)
    }

    override suspend fun getQPacksFromThemeCount(themeId: Long): Int {
        return qPackDao.getQPacksFromThemeCount(themeId)
    }
}