package ru.samtakoy.core.database.room.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.samtakoy.core.database.DB_NAME
import ru.samtakoy.core.database.room.MyRoomDb
import ru.samtakoy.core.di.modules.AppModule
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DatabaseModule {

    @Provides
    @Singleton
    fun getDataBase(ctx: Context): MyRoomDb {
        return Room.databaseBuilder(ctx, MyRoomDb::class.java, DB_NAME)
                //.addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
    }

}