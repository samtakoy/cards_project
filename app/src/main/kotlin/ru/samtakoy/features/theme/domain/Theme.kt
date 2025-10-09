package ru.samtakoy.features.theme.domain

data class Theme(
    val id: Long,
    val title: String,
    val parentId: Long
)