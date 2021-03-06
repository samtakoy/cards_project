package ru.samtakoy.core.domain.utils

import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity


class LearnCourseCardsIdsPair(
        val learnCourse: LearnCourseEntity,
        val cardIds: List<Long>
) {
    fun hasNotInCards(): Boolean {
        return learnCourse.hasNotInCards(cardIds)
    }

    fun getNotInCards(): List<Long> {
        return learnCourse.getNotInCards(cardIds)
    }


}