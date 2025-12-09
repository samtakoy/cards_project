package ru.samtakoy.download.di

import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.remote.di.NoBaseUrlKtorfit
import ru.samtakoy.download.data.FileApi
import ru.samtakoy.download.data.FileRepositoryImpl
import ru.samtakoy.download.data.createFileApi
import ru.samtakoy.download.domain.DownloadFileUseCase
import ru.samtakoy.download.domain.DownloadFileUseCaseImpl
import ru.samtakoy.download.domain.FileRepository

fun downloadFeatureModule() = module {
    single<FileApi> {
        val ktorfit = get<Ktorfit>(qualifier = NoBaseUrlKtorfit)
        ktorfit.createFileApi()
    }

    factoryOf(::FileRepositoryImpl) bind FileRepository::class
    factoryOf(::DownloadFileUseCaseImpl) bind DownloadFileUseCase::class
}