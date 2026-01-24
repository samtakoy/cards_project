package ru.samtakoy.data.learncourse.model

internal enum class LearnCourseModeEntity(dbId: Int) {
    PREPARING(1),
    LEARN_WAITING(2),
    LEARNING(3),
    REPEAT_WAITING(4),
    REPEATING(5),
    COMPLETED(6);

    val dbId: Int

    init {
        this.dbId = dbId
    }

    companion object {
        private val sIdToEnumMap: MutableMap<Int, LearnCourseModeEntity> = HashMap<Int, LearnCourseModeEntity>()

        init {
            for (lpm in entries) {
                sIdToEnumMap.put(lpm.dbId, lpm)
            }
        }

        fun valueOfId(dbId: Int): LearnCourseModeEntity? {
            return sIdToEnumMap.get(dbId)
        }
    }
}
