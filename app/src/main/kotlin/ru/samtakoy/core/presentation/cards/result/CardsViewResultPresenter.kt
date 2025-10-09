package ru.samtakoy.core.presentation.cards.result

import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule

// TODO remove
interface CardsViewResultPresenter {
    interface Callbacks {
        fun onResultOk(newSchedule: Schedule)
    }
}