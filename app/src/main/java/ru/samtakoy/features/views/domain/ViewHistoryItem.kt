package ru.samtakoy.features.views.domain

/**
 * @param qPackId id пака, если это просомтр карточек пака
 * */
data class ViewHistoryItem(
    val id: Long,
    val qPackId: Long,
    val viewedCardIds: List<Long>,
    val todoCardIds: List<Long>,
    val errorCardIds: List<Long>,
    val addedToFavsCardIds: List<Long>,
    var lastViewDate: java.util.Date
)