package ru.samtakoy.core.business.impl

import io.reactivex.Flowable
import ru.samtakoy.core.business.QPacksRepository
import ru.samtakoy.core.database.room.MyRoomDb
import ru.samtakoy.core.database.room.entities.QPackEntity

class QPacksRepositoryImpl(
        private val db: MyRoomDb
) : QPacksRepository {

    override fun addQPack(qPack: QPackEntity): Long {
        val qPackId = db.qPackDao().addQPack(qPack);
        qPack.id = qPackId
        return qPackId
    }

    override fun getQPack(qPackId: Long): QPackEntity? =
            db.qPackDao().getQPack(qPackId)
    //mCachedQPack = ContentProviderHelper.getConcretePack(mCtx.getContentResolver(), qPackId)

    override fun getQPackRx(qPackId: Long): Flowable<QPackEntity> =
            db.qPackDao().getQPackRx(qPackId)


    override fun deletePack(qPackId: Long): Boolean =
            db.qPackDao().deleteQPackById(qPackId) > 0
    //ContentProviderHelper.deletePack(mCtx, qPackId)


    override fun updateQPack(qPack: QPackEntity) =
            db.qPackDao().updateQPack(qPack)
    //ContentProviderHelper.saveQPack(mCtx.getContentResolver(), qPack)

    override fun isPackExists(qPackId: Long): Boolean =
            db.qPackDao().isPackExists(qPackId) > 0

    override fun hasAnyQPack(): Boolean =
            db.qPackDao().getAllQPackCount() > 0
    //return ContentProviderHelper.isAnyPackExists(mCtx.getContentResolver())


    override fun getQPacksFromTheme(themeId: Long): List<QPackEntity> =
            db.qPackDao().getQPacksFromTheme(themeId)
    //return ContentProviderHelper.getCurQPacks(mCtx, themeId)

    override fun getQPacksFromThemeRx(themeId: Long): Flowable<List<QPackEntity>> =
            db.qPackDao().getQPacksFromThemeRx(themeId)

    override fun getAllQPacks(): Flowable<List<QPackEntity>> {
        val allQPacks = db.qPackDao().getAllQPacks()
        return allQPacks
    }

    override fun getAllQPacksByLastViewDateAsc(): Flowable<List<QPackEntity>> =
            db.qPackDao().getAllQPacksByLastViewDateAsc()

    override fun getAllQPacksByCreationDateDesc(): Flowable<List<QPackEntity>> =
            db.qPackDao().getAllQPacksByCreationDateDesc()

    /*{
        return Observable.fromCallable {
            val result = ContentProviderHelper.getAllQPacks(mCtx)
            MyLog.add("-- qPacks: " + result.size)
            result
        }.flatMap { qPacks: List<QPack?>? -> Observable.fromIterable(qPacks) }
    }*/

}