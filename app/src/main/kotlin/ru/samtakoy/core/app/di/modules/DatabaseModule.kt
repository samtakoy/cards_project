package ru.samtakoy.core.app.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.features.database.data.DB_NAME
import ru.samtakoy.features.database.data.MyRoomDb
import ru.samtakoy.features.database.data.TransactionRepository
import ru.samtakoy.features.database.data.TransactionRepositoryImpl
import javax.inject.Singleton

@Module(includes = [AppModule::class])
internal interface DatabaseModule {

    @Binds
    fun bindsTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun getDataBase(ctx: Context): MyRoomDb {
            return Room.databaseBuilder(ctx, MyRoomDb::class.java, DB_NAME)
                // .addMigrations(MIGRATION_5_6)
                .fallbackToDestructiveMigration(true)
                .build()
        }
    }
}