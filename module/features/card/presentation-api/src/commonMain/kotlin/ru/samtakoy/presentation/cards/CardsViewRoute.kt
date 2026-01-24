package ru.samtakoy.presentation.cards

import kotlinx.serialization.Serializable
import ru.samtakoy.navigation.presentation.RootRoute

@Serializable
data class CardsViewRoute(
    val params: CardsViewParams
) : RootRoute