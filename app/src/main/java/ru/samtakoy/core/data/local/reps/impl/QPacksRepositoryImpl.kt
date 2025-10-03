package ru.samtakoy.core.data.local.reps.impl

import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.samtakoy.core.data.local.database.room.MyRoomDb
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds
import ru.samtakoy.core.data.local.reps.QPacksRepository
import javax.inject.Inject

class QPacksRepositoryImpl @Inject constructor(
    private val db: MyRoomDb
) : QPacksRepository {

    override fun addQPack(qPack: QPackEntity): Long {
        return db.qPackDao().addQPack(qPack)
    }

    override fun getQPackRx(qPackId: Long): Flowable<QPackEntity> {
        return db.qPackDao().getQPackRx(qPackId)
    }

    override suspend fun getQPack(qPackId: Long): QPackEntity? {
        return db.qPackDao().getQPack(qPackId)
    }

    override fun getQPackWithCardIdsAsFlow(qPackId: Long): Flow<QPackWithCardIds> {
        return db.qPackDao().getQPackWithCardIdsAsFlow(qPackId)
    }

    override suspend fun deletePack(qPackId: Long) {
        db.qPackDao().deleteQPackById(qPackId)
    }

    override fun updateQPack(qPack: QPackEntity) =
            db.qPackDao().updateQPack(qPack)

    override fun updateQPackFavorite(qPackId: Long, favorite: Int) {
        db.qPackDao().updateQPackFavorite(qPackId, favorite)
    }

    override suspend fun updateQPackViewCount(qPackId: Long, currentTime: java.util.Date) {
        db.qPackDao().updateQPackViewCount(qPackId, currentTime)
    }

    override fun isPackExists(qPackId: Long): Boolean =
            db.qPackDao().isPackExists(qPackId) > 0

    override fun getQPacksFromTheme(themeId: Long): List<QPackEntity> =
            db.qPackDao().getQPacksFromTheme(themeId)

    override fun getQPacksFromThemeAsFlow(themeId: Long): Flow<List<QPackEntity>> {
        return db.qPackDao().getQPacksFromThemeAsFlow(themeId)
    }

    override suspend fun getAllQPacks(): List<QPackEntity> {
        return db.qPackDao().getAllQPacks()
    }

    override fun getAllQPacksByLastViewDateAscAsFlow(onlyFavorites: Boolean): Flow<List<QPackEntity>> {
        return if (onlyFavorites) {
            db.qPackDao().getAllFavQPacksByLastViewDateAscAsFlow()
        } else {
            db.qPackDao().getAllQPacksByLastViewDateAscAsFlow()
        }
    }

    override fun getAllQPacksByLastViewDateAscFilteredAsFlow(
        searchString: String,
        onlyFavorites: Boolean
    ): Flow<List<QPackEntity>> {
        return if (onlyFavorites) {
            db.qPackDao().getAllFavQPacksByLastViewDateAscFilteredAsFlow("%$searchString%")
        } else {
            db.qPackDao().getAllQPacksByLastViewDateAscFilteredAsFlow("%$searchString%")
        }
    }

    override fun getAllQPacksByCreationDateDescAsFlow(onlyFavorites: Boolean): Flow<List<QPackEntity>> {
        return if (onlyFavorites) {
            db.qPackDao().getAllFavQPacksByCreationDateDescAsFlow()
        } else {
            db.qPackDao().getAllQPacksByCreationDateDescAsFlow()
        }
    }

    override fun getAllQPacksByCreationDateDescFilteredAsFlow(
        searchString: String,
        onlyFavorites: Boolean
    ): Flow<List<QPackEntity>> {
        return if (onlyFavorites) {
            db.qPackDao().getAllFavQPacksByCreationDateDescFilteredAsFlow("%$searchString%")
        } else {
            db.qPackDao().getAllQPacksByCreationDateDescFilteredAsFlow("%$searchString%")
        }
    }

    override suspend fun getAllQPacksIdsByCreationDateDescWithFavs(): List<Long> {
        return db.qPackDao().getAllQPacksIdsByCreationDateDescWithFavs()
    }

    override fun getAllQPacksIdsByCreationDateDescWithFavsAsFlow(): Flow<List<Long>> {
        return db.qPackDao().getAllQPacksIdsByCreationDateDescWithFavsAsFlow().distinctUntilChanged()
    }

    override suspend fun getQPacksByIds(ids: List<Long>): List<QPackEntity> {
        return db.qPackDao().getQPacksByIds(ids)
    }
}