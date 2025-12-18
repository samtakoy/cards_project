package ru.samtakoy.data.common.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers

internal fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<MyRoomDb> {
    val context = context.applicationContext
    val dbFile = context.getDatabasePath(DB_NAME)
    return Room.databaseBuilder<MyRoomDb>(context, dbFile.absolutePath)
        .setQueryCoroutineContext(Dispatchers.IO)
}