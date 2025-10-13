package ru.samtakoy.data.common.transaction

interface TransactionRepository {
    suspend fun <T> withTransaction(block: suspend () -> T): T
    fun <T> withTransactionSync(block: () -> T): T
}