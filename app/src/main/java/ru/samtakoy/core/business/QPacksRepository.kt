package ru.samtakoy.core.business

import io.reactivex.Flowable
import io.reactivex.Single
import ru.samtakoy.core.database.room.entities.QPackEntity

interface QPacksRepository {


    fun addQPack(qPack: QPackEntity): Long
    fun getQPack(qPackId: Long): QPackEntity?
    fun getQPackRx(qPackId: Long): Flowable<QPackEntity>
    fun deletePack(qPackId: Long): Boolean
    fun updateQPack(qPack: QPackEntity)

    fun isPackExists(qPackId: Long): Boolean
    fun hasAnyQPack(): Boolean
    fun getQPacksFromTheme(themeId: Long): List<QPackEntity>
    fun getQPacksFromThemeRx(themeId: Long): Flowable<List<QPackEntity>>

    fun getAllQPacks(): Single<List<QPackEntity>>
    fun getAllQPacksByLastViewDateAsc(): Flowable<List<QPackEntity>>
    fun getAllQPacksByCreationDateDesc(): Flowable<List<QPackEntity>>


}