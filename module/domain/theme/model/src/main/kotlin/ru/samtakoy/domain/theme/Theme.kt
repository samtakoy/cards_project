package ru.samtakoy.domain.theme

data class Theme(
    val id: Long,
    val title: String,
    val parentId: Long
)