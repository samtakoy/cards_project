package ru.samtakoy.data.common.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.common.db.transaction.TransactionRepositoryImpl
import ru.samtakoy.data.common.transaction.TransactionRepository

internal fun commonDataModule() = module {
    singleOf(::TransactionRepositoryImpl) bind TransactionRepository::class
}