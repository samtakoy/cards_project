package ru.samtakoy.data.common.db.transaction

import androidx.room.deferredTransaction
import androidx.room.immediateTransaction
import androidx.room.useReaderConnection
import androidx.room.useWriterConnection
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.common.transaction.TransactionRepository

internal class TransactionRepositoryImpl(
    private val database: MyRoomDb
) : TransactionRepository {

    override suspend fun <T> withTransaction(block: suspend () -> T): T {
        return database.useWriterConnection { transactor ->
            transactor.immediateTransaction {
                block()
            }
        }
    }

    override suspend fun <T> withReadTransaction(block: suspend () -> T): T {
        return database.useReaderConnection { transactor ->
            transactor.deferredTransaction {
                block()
            }
        }
    }
}