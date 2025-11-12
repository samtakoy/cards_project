package ru.samtakoy.data.common.db

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import java.io.File

internal fun getDatabaseBuilder(): RoomDatabase.Builder<MyRoomDb> {
    // Вариант 3: Сохранение в app data директории
    val appDataDir = File(System.getProperty("user.home"), ".cards")
    appDataDir.mkdirs() // Создаём директорию если её нет
    val dbFile = File(appDataDir, DB_NAME)

    return Room.databaseBuilder<MyRoomDb>(
        name = dbFile.absolutePath
    ).setQueryCoroutineContext(Dispatchers.IO)
}