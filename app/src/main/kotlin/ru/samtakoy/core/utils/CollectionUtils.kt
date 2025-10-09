package ru.samtakoy.core.utils

import java.util.Collections

object CollectionUtils {

    fun createShuffledIds(cardIds: List<Long>): List<Long> {
        val shuffledIds: List<Long> = ArrayList<Long>(cardIds)
        Collections.shuffle(shuffledIds)
        return shuffledIds
    }
}