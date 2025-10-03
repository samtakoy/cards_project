package ru.samtakoy.core.data.local.reps

interface TransactionRepository {
    suspend fun <T> withTransaction(block: suspend () -> T): T
}