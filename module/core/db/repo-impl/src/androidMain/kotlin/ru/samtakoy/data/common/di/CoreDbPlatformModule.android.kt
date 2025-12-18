package ru.samtakoy.data.common.di

import android.content.Context
import org.koin.dsl.module
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.common.db.getDatabaseBuilder
import ru.samtakoy.data.common.db.getRoomDatabase

internal actual fun coreDbPlatformModule() = module {
    single<MyRoomDb> {
        getRoomDatabase(getDatabaseBuilder(context = get<Context>()))
    }
}