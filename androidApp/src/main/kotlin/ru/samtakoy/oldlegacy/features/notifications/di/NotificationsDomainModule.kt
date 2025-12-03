package ru.samtakoy.oldlegacy.features.notifications.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.samtakoy.oldlegacy.features.notifications.learn_courses.LearnsApi
import ru.samtakoy.oldlegacy.features.notifications.learn_courses.NewRepeatsApi
import ru.samtakoy.oldlegacy.features.notifications.learn_courses.UncompletedTaskApi

// TODO тут все пересмотреть и переделать (нейминг и вынесение нотификаций в репозиторий)
fun notificationsDomainModule() = module {
    factoryOf(::LearnsApi)
    factoryOf(::NewRepeatsApi)
    factoryOf(::UncompletedTaskApi)
}