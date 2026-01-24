package ru.samtakoy.domain.cardtag

fun Tag.toStringKey(): String {
    return title.tagTitleToKey()
}

fun String.tagTitleToKey(): String {
    return trim().lowercase()
}