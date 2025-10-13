package ru.samtakoy.domain.learncourse.schedule.serialize

import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.learncourse.schedule.ScheduleItem
import ru.samtakoy.domain.learncourse.schedule.ScheduleTimeUnit

fun ParcelableSchedule.toDomain(): Schedule {
    return Schedule(
        items.map(ParcelableSchedule.Item::toDomain)
    )
}

fun ParcelableSchedule?.toDomainOrEmpty(): Schedule {
    this ?: return Schedule(emptyList())
    return Schedule(
        items.map(ParcelableSchedule.Item::toDomain)
    )
}

fun ParcelableSchedule.Item.toDomain(): ScheduleItem {
    return ScheduleItem(
        dimension = dimension,
        timeUnit = timeUnit.toDomain()
    )
}

fun ParcelableSchedule.TimeUnit.toDomain(): ScheduleTimeUnit {
    return when (this) {
        ParcelableSchedule.TimeUnit.MINUTE -> ScheduleTimeUnit.MINUTE
        ParcelableSchedule.TimeUnit.HOUR -> ScheduleTimeUnit.HOUR
        ParcelableSchedule.TimeUnit.DAY -> ScheduleTimeUnit.DAY
        ParcelableSchedule.TimeUnit.WEEK -> ScheduleTimeUnit.WEEK
        ParcelableSchedule.TimeUnit.MONTH -> ScheduleTimeUnit.MONTH
        ParcelableSchedule.TimeUnit.YEAR -> ScheduleTimeUnit.YEAR
    }
}

fun Schedule.toParcelable(): ParcelableSchedule {
    return ParcelableSchedule(
        items.map(ScheduleItem::toParcelable)
    )
}

fun ScheduleItem.toParcelable(): ParcelableSchedule.Item {
    return ParcelableSchedule.Item(
        dimension = dimension,
        timeUnit = timeUnit.toParcelable()
    )
}

fun ScheduleTimeUnit.toParcelable(): ParcelableSchedule.TimeUnit {
    return when (this) {
        ScheduleTimeUnit.MINUTE -> ParcelableSchedule.TimeUnit.MINUTE
        ScheduleTimeUnit.HOUR -> ParcelableSchedule.TimeUnit.HOUR
        ScheduleTimeUnit.DAY -> ParcelableSchedule.TimeUnit.DAY
        ScheduleTimeUnit.WEEK -> ParcelableSchedule.TimeUnit.WEEK
        ScheduleTimeUnit.MONTH -> ParcelableSchedule.TimeUnit.MONTH
        ScheduleTimeUnit.YEAR -> ParcelableSchedule.TimeUnit.YEAR
    }
}