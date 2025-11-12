package ru.samtakoy.presentation.cards

import kotlinx.serialization.Serializable
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.navigation.RootRoute

@Serializable
data class CardsViewResultRoute(
    val params: CardsViewResultParams
) : RootRoute