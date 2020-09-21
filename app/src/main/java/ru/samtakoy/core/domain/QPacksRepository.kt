package ru.samtakoy.core.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds

interface QPacksRepository {

    fun addQPack(qPack: QPackEntity): Long

    fun getQPack(qPackId: Long): Single<QPackEntity>
    fun getQPackRx(qPackId: Long): Flowable<QPackEntity>
    fun getQPackWithCardIdsRx(qPackId: Long): Flowable<QPackWithCardIds>;
    fun deletePack(qPackId: Long): Completable

    fun updateQPack(qPack: QPackEntity)
    fun updateQPackViewCount(qPackId: Long, currentTime: java.util.Date)

    fun isPackExists(qPackId: Long): Boolean

    fun getQPacksFromTheme(themeId: Long): List<QPackEntity>
    fun getQPacksFromThemeRx(themeId: Long): Flowable<List<QPackEntity>>

    fun getAllQPacks(): Single<List<QPackEntity>>
    fun getAllQPacksByLastViewDateAsc(): Flowable<List<QPackEntity>>
    fun getAllQPacksByCreationDateDesc(): Flowable<List<QPackEntity>>


}