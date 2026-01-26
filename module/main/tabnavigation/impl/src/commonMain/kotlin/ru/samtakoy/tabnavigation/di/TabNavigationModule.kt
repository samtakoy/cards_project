package ru.samtakoy.tabnavigation.di

import org.koin.dsl.module
import ru.samtakoy.tabnavigation.presentation.navigator.TabNavigatorHost
import ru.samtakoy.tabnavigation.presentation.navigator.TabNavigatorHostImpl
import ru.samtakoy.tabnavigation.presentation.navigator.TabNavigatorImpl

fun tabNavigationModule() = module {
    factory { TabNavigatorImpl() }
    factory<TabNavigatorHost> { TabNavigatorHostImpl(get()) }
 }