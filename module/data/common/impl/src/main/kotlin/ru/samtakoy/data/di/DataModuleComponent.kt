package ru.samtakoy.data.di

import dagger.Component
import ru.samtakoy.common.di.CommonUtilsComponent
import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.data.card.di.CardDataModule
import ru.samtakoy.data.cardtag.TagsRepository
import ru.samtakoy.data.cardtag.di.CardTagDataModule
import ru.samtakoy.data.common.di.CommonDataModule
import ru.samtakoy.data.common.transaction.TransactionRepository
import ru.samtakoy.data.learncourse.CourseViewRepository
import ru.samtakoy.data.learncourse.CoursesRepository
import ru.samtakoy.data.learncourse.di.LearnCourseDataModule
import ru.samtakoy.data.learncourseview.di.LearnCourseViewDataModule
import ru.samtakoy.data.qpack.QPacksRepository
import ru.samtakoy.data.qpack.di.QPackDataModule
import ru.samtakoy.data.theme.ThemesRepository
import ru.samtakoy.data.theme.di.ThemeDataModule
import ru.samtakoy.data.view.ViewHistoryRepository
import ru.samtakoy.data.view.di.ViewDataModule
import ru.samtakoy.platform.di.PlatformComponent

@DataScope
@Component(
    modules = [
        CommonDataModule::class,
        CardDataModule::class,
        CardTagDataModule::class,
        QPackDataModule::class,
        ThemeDataModule::class,
        LearnCourseDataModule::class,
        LearnCourseViewDataModule::class,
        ViewDataModule::class
    ],
    dependencies = [
        CommonUtilsComponent::class,
        PlatformComponent::class,
    ]
)
interface DataModuleComponent {
    fun transactionRepository(): TransactionRepository
    fun cardsRepository(): CardsRepository
    fun tagsRepository(): TagsRepository
    fun qPacksRepository(): QPacksRepository
    fun themesRepository(): ThemesRepository
    fun coursesRepository(): CoursesRepository
    fun courseViewRepository(): CourseViewRepository
    fun viewHistoryRepository(): ViewHistoryRepository
}