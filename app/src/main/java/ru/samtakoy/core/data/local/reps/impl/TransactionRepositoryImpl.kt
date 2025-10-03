package ru.samtakoy.core.data.local.reps.impl

import androidx.room.withTransaction
import ru.samtakoy.core.data.local.database.room.MyRoomDb
import ru.samtakoy.core.data.local.reps.TransactionRepository
import javax.inject.Inject

internal class TransactionRepositoryImpl @Inject constructor(
    private val database: MyRoomDb
) : TransactionRepository {

    override suspend fun <T> withTransaction(block: suspend () -> T): T {
        return database.withTransaction<T>(block)
    }
}