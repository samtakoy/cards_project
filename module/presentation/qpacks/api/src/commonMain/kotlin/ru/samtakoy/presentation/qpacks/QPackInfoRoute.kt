package ru.samtakoy.presentation.qpacks

import kotlinx.serialization.Serializable
import ru.samtakoy.presentation.navigation.RootRoute

@Serializable
data class QPackInfoRoute(val qPackId: Long) : RootRoute