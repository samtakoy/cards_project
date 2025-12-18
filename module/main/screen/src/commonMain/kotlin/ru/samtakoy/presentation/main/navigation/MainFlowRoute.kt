package ru.samtakoy.presentation.main.navigation

import kotlinx.serialization.Serializable

sealed interface MainFlowRoute {
    // TODO везде добавить serialized name
    @Serializable
    object Tabs : MainFlowRoute
    /** TODO используется? */
    @Serializable
    object TopScreens : MainFlowRoute
}