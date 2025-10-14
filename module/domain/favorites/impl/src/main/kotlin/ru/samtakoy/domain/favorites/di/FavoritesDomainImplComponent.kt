package ru.samtakoy.domain.favorites.di

import dagger.Component
import ru.samtakoy.data.di.DataModuleApiComponent

@FavoritesDomainScope
@Component(
    modules = [FavoritesDomainModule::class],
    dependencies = [
        DataModuleApiComponent::class
    ]
)
interface FavoritesDomainImplComponent : FavoritesDomainApiComponent