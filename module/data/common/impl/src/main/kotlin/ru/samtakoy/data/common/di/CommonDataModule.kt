package ru.samtakoy.data.common.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.data.common.db.DB_NAME
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.common.db.transaction.TransactionRepositoryImpl
import ru.samtakoy.data.common.transaction.TransactionRepository
import ru.samtakoy.data.di.DataScope

@Module
internal interface CommonDataModule {
    @Binds
    fun bindsTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    companion object {

        @JvmStatic
        @Provides
        @DataScope
        fun getDataBase(ctx: Context): MyRoomDb {
            return Room.databaseBuilder(ctx, MyRoomDb::class.java, DB_NAME)
                // .addMigrations(MIGRATION_5_6)
                .fallbackToDestructiveMigration(true)
                .build()
        }
    }
}