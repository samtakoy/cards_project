package ru.samtakoy.domain.view

data class ViewHistoryItemWithInfo(
    val viewItem: ViewHistoryItem,
    val qPackTitle: String,
    val themeTitle: String?
)