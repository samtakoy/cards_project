package ru.samtakoy.data.common.di

import android.content.Context
import androidx.room.Room
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.common.db.DB_NAME
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.common.db.transaction.TransactionRepositoryImpl
import ru.samtakoy.data.common.transaction.TransactionRepository

internal fun commonDataModule() = module {
    singleOf(::TransactionRepositoryImpl) bind TransactionRepository::class
}