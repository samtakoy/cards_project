package ru.samtakoy.presentation.cards

import kotlinx.serialization.Serializable
import ru.samtakoy.presentation.cards.view.model.CardViewMode

@Serializable
data class CardsViewResultParams(
    val viewHistoryId: Long,
    val viewMode: CardViewMode
)