package ru.samtakoy.presentation.main.navigation

import kotlinx.serialization.Serializable

sealed interface MainFlowRoute {
    @Serializable
    object Tabs : MainFlowRoute
}