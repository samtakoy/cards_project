package ru.samtakoy.features.database.data

import androidx.room.withTransaction
import javax.inject.Inject

internal class TransactionRepositoryImpl @Inject constructor(
    private val database: MyRoomDb
) : TransactionRepository {

    override suspend fun <T> withTransaction(block: suspend () -> T): T {
        return if (database.inTransaction()) {
            block.invoke()
        } else {
            database.withTransaction<T>(block)
        }
    }

    override fun <T> withTransactionSync(block: () -> T): T {
        return if (database.inTransaction()) {
            block.invoke()
        } else {
            database.runInTransaction<T>(block)
        }
    }
}