package ru.samtakoy.domain.card.domain.model

import ru.samtakoy.domain.cardtag.Tag

data class CardWithTags(
    val card: Card,
    val tags: List<Tag>
)