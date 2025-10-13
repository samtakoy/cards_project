package ru.samtakoy.data.common.db.transaction

import androidx.room.withTransaction
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.common.transaction.TransactionRepository
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