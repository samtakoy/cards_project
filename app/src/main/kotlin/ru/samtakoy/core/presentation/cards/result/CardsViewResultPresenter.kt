package ru.samtakoy.core.presentation.cards.result

import ru.samtakoy.domain.learncourse.schedule.Schedule

// TODO remove
interface CardsViewResultPresenter {
    interface Callbacks {
        fun onResultOk(newSchedule: Schedule)
    }
}