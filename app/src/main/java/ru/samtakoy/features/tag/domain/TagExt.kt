package ru.samtakoy.features.tag.domain

fun Tag.toStringKey(): String {
    return title.tagTitleToKey()
}

fun String.tagTitleToKey(): String {
    return trim().lowercase()
}