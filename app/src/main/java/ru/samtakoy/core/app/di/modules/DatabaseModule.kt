package ru.samtakoy.core.app.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.samtakoy.core.data.local.database.DB_NAME
import ru.samtakoy.core.data.local.database.room.MyRoomDb
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