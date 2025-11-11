package ru.samtakoy.data.common.transaction

interface TransactionRepository {
    suspend fun <T> withTransaction(block: suspend () -> T): T
    // Если нужна read-only транзакция
    suspend fun <T> withReadTransaction(block: suspend () -> T): T
}