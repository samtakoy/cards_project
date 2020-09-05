package ru.samtakoy.core.business

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ru.samtakoy.core.database.room.entities.QPackEntity
import ru.samtakoy.core.database.room.entities.other.QPackWithCardIds

interface QPacksRepository {


    //TODO  -> RxJava
    fun addQPack(qPack: QPackEntity): Long

    //TODO  -> RxJava
    fun getQPack(qPackId: Long): Single<QPackEntity>
    fun getQPackRx(qPackId: Long): Flowable<QPackEntity>
    fun getQPackWithCardIdsRx(qPackId: Long): Flowable<QPackWithCardIds>;
    fun deletePack(qPackId: Long): Completable

    //TODO  -> RxJava
    fun updateQPack(qPack: QPackEntity)
    fun updateQPackViewCount(qPackId: Long, currentTime: java.util.Date)

    //TODO  -> RxJava
    fun isPackExists(qPackId: Long): Boolean

    //TODO  -> RxJava
    fun hasAnyQPack(): Boolean
    fun getQPacksFromTheme(themeId: Long): List<QPackEntity>
    fun getQPacksFromThemeRx(themeId: Long): Flowable<List<QPackEntity>>

    fun getAllQPacks(): Single<List<QPackEntity>>
    fun getAllQPacksByLastViewDateAsc(): Flowable<List<QPackEntity>>
    fun getAllQPacksByCreationDateDesc(): Flowable<List<QPackEntity>>


}