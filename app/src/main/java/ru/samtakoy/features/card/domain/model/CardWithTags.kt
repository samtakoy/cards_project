package ru.samtakoy.features.card.domain.model

import ru.samtakoy.features.tag.domain.Tag

data class CardWithTags(
    val card: Card,
    val tags: List<Tag>
)