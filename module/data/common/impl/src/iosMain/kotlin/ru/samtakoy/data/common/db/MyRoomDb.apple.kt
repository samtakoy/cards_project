package ru.samtakoy.data.common.db

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal fun getDatabaseBuilder(): RoomDatabase.Builder<MyRoomDb> {
    // Путь в Documents директорию iOS приложения
    @OptIn(ExperimentalForeignApi::class)
    val documentsDir = NSFileManager.defaultManager.URLForDirectory(
        NSDocumentDirectory,
        NSUserDomainMask,
        null,
        true,
        null
    )?.path
    val dbPath = "${documentsDir}/${DB_NAME}"

    return Room.databaseBuilder<MyRoomDb>(
        name = dbPath
    ).setQueryCoroutineContext(
        // iOS эквивалент Dispatchers.IO
        kotlinx.coroutines.CoroutineScope(
            @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
            kotlinx.coroutines.newSingleThreadContext("room-io")
        ).coroutineContext
    )
}