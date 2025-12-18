package ru.samtakoy.data.common.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.common.db.transaction.TransactionRepositoryImpl
import ru.samtakoy.domain.common.transaction.TransactionRepository

internal fun coreDbCommonModule() = module {
    singleOf(::TransactionRepositoryImpl) bind TransactionRepository::class
}