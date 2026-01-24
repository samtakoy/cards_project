package ru.samtakoy.presentation.cards

import kotlinx.serialization.Serializable
import ru.samtakoy.navigation.presentation.RootRoute

@Serializable
data class CardsViewResultRoute(
    val params: CardsViewResultParams
) : RootRoute