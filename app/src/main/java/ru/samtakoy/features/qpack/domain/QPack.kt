package ru.samtakoy.features.qpack.domain

import java.util.Date

data class QPack(
    val id: Long,
    val themeId: Long,
    val path: String,
    val fileName: String,
    val title: String,
    val desc: String,
    val creationDate: Date,
    val viewCount: Int,
    val lastViewDate: Date,
    val favorite: Int
)