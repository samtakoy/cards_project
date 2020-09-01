package ru.samtakoy.core.business.utils

import ru.samtakoy.core.database.room.entities.LearnCourseEntity


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