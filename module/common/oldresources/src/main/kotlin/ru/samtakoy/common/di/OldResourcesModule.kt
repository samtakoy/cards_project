package ru.samtakoy.common.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.resources.ResourcesImpl

@Deprecated("Оставлено для oldlegacy")
fun oldResourcesModule() = module {
    factoryOf(::ResourcesImpl) bind Resources::class
}