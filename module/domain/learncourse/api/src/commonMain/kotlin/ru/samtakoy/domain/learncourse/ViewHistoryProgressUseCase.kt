package ru.samtakoy.domain.learncourse

import ru.samtakoy.domain.view.ViewHistoryItem

interface ViewHistoryProgressUseCase {
    /**
     * @return идентификатор карточки, к которой произошел возврат.
     * Если возврат не возможен, возвращает null.
     * */
    suspend fun rollbackToPrevCard(viewItemId: Long): Long?
    suspend fun makeViewedCurCard(
        viewItemId: Long,
        curCardId: Long,
        withError: Boolean
    ): ViewHistoryItem?
}