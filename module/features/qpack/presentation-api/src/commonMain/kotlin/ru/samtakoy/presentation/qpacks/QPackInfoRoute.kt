package ru.samtakoy.presentation.qpacks

import kotlinx.serialization.Serializable
import ru.samtakoy.navigation.presentation.RootRoute

@Serializable
data class QPackInfoRoute(val qPackId: Long) : RootRoute