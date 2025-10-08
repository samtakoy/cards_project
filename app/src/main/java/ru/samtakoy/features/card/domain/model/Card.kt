package ru.samtakoy.features.card.domain.model

data class Card(
    val id: Long,
    val qPackId: Long,
    val question: String,
    val answer: String,
    val aImages: List<String>,
    val comment: String,
    val favorite: Int
)