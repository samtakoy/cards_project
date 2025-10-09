package ru.samtakoy.features.views.domain

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