package ru.samtakoy.common.utils

object CollectionUtils {

    fun createShuffledIds(cardIds: List<Long>): List<Long> {
        return cardIds.shuffled()
    }
}