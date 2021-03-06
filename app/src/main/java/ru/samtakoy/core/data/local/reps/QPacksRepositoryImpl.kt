package ru.samtakoy.core.data.local.reps

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ru.samtakoy.core.data.local.database.room.MyRoomDb
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds
import ru.samtakoy.core.domain.QPacksRepository

class QPacksRepositoryImpl(
        private val db: MyRoomDb
) : QPacksRepository {

    override fun addQPack(qPack: QPackEntity): Long {
        val qPackId = db.qPackDao().addQPack(qPack);
        qPack.id = qPackId
        return qPackId
    }

    override fun getQPack(qPackId: Long): Single<QPackEntity> =
            db.qPackDao().getQPack(qPackId)

    override fun getQPackRx(qPackId: Long): Flowable<QPackEntity> =
            db.qPackDao().getQPackRx(qPackId)

    override fun getQPackWithCardIdsRx(qPackId: Long): Flowable<QPackWithCardIds> =
            db.qPackDao().getQPackWithCardIds(qPackId)

    override fun deletePack(qPackId: Long): Completable {
        return Completable.fromCallable { db.qPackDao().deleteQPackById(qPackId) > 0 }
    }

    override fun updateQPack(qPack: QPackEntity) =
            db.qPackDao().updateQPack(qPack)

    override fun updateQPackViewCount(qPackId: Long, currentTime: java.util.Date) =
            db.qPackDao().updateQPackViewCount(qPackId, currentTime)

    override fun isPackExists(qPackId: Long): Boolean =
            db.qPackDao().isPackExists(qPackId) > 0

    override fun getQPacksFromTheme(themeId: Long): List<QPackEntity> =
            db.qPackDao().getQPacksFromTheme(themeId)

    override fun getQPacksFromThemeRx(themeId: Long): Flowable<List<QPackEntity>> =
            db.qPackDao().getQPacksFromThemeRx(themeId)

    override fun getAllQPacks(): Single<List<QPackEntity>> {
        val allQPacks = db.qPackDao().getAllQPacks()
        return allQPacks
    }

    override fun getAllQPacksByLastViewDateAsc(): Flowable<List<QPackEntity>> =
            db.qPackDao().getAllQPacksByLastViewDateAsc()

    override fun getAllQPacksByCreationDateDesc(): Flowable<List<QPackEntity>> =
            db.qPackDao().getAllQPacksByCreationDateDesc()


}