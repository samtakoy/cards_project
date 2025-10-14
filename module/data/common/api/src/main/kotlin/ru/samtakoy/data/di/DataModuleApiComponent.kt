package ru.samtakoy.data.di

import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.data.cardtag.TagsRepository
import ru.samtakoy.data.common.transaction.TransactionRepository
import ru.samtakoy.data.learncourse.CourseViewRepository
import ru.samtakoy.data.learncourse.CoursesRepository
import ru.samtakoy.data.qpack.QPacksRepository
import ru.samtakoy.data.theme.ThemesRepository
import ru.samtakoy.data.view.ViewHistoryRepository

interface DataModuleApiComponent {
    fun transactionRepository(): TransactionRepository
    fun cardsRepository(): CardsRepository
    fun tagsRepository(): TagsRepository
    fun qPacksRepository(): QPacksRepository
    fun themesRepository(): ThemesRepository
    fun coursesRepository(): CoursesRepository
    fun courseViewRepository(): CourseViewRepository
    fun viewHistoryRepository(): ViewHistoryRepository
}