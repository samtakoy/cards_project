package ru.samtakoy.domain.qpack

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class QPack(
    val id: Long,
    val themeId: Long,
    val path: String,
    val fileName: String,
    val title: String,
    val desc: String,
    val creationDate: Instant,
    val viewCount: Int,
    val lastViewDate: Instant,
    val favorite: Int
)