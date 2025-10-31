package ru.samtakoy.platform.importcards.di

import android.content.Context
import androidx.work.WorkerParameters
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.platform.importcards.ImportCardsFromZipTask
import ru.samtakoy.platform.importcards.ImportCardsFromZipTaskAndroidImpl
import ru.samtakoy.platform.importcards.ImportCardsFromZipWorker

fun importCardsFromZipPlatformModule() = module {
    /* worker { (context: Context, params: WorkerParameters) ->
        ImportCardsFromZipWorker(get(), get(), get(), get(), context, params)
    }*/

    singleOf(::ImportCardsFromZipTaskAndroidImpl) bind ImportCardsFromZipTask::class
}