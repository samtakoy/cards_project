package ru.samtakoy.domain.task.model

sealed interface TaskStateData {
    /** Статус не инициализировался и не известен */
    data object Unknown : TaskStateData
    /** Задача не активна */
    data object NotActive : TaskStateData
    /** Еще не стартовала, но уже запущена попытка старта */
    data object Init : TaskStateData

    /** Выполняется
     * @param progress прогресс от 0 до 1f
     * */
    data class ActiveStatus(
        val message: String,
        val progress: Float? = null
    ) : TaskStateData

    // Финальные состояния:
    data class Error(val message: String) : TaskStateData
    data object Success : TaskStateData
    data object Cancelled : TaskStateData
}