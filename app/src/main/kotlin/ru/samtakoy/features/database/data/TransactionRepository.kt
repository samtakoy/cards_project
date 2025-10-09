package ru.samtakoy.features.database.data

interface TransactionRepository {
    suspend fun <T> withTransaction(block: suspend () -> T): T
    fun <T> withTransactionSync(block: () -> T): T
}