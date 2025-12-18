package ru.samtakoy.data.common.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.common.db.getDatabaseBuilder
import ru.samtakoy.data.common.db.getRoomDatabase

internal actual fun coreDbPlatformModule(): Module = module {
    single<MyRoomDb> {
        getRoomDatabase(getDatabaseBuilder())
    }
}