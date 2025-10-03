package ru.samtakoy.core.presentation.cards.result

import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule

// TODO remove
interface CardsViewResultPresenter {
    interface Callbacks {
        fun onResultOk(newSchedule: Schedule)
    }
}