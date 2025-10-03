package ru.samtakoy.features.views.domain

data class ViewHistoryItemWithInfo(
    val viewItem: ViewHistoryItem,
    val qPackTitle: String,
    val themeTitle: String?
)