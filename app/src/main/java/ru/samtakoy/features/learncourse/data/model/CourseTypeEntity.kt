package ru.samtakoy.features.learncourse.data.model

enum class CourseTypeEntity(dbId: Int) {
    // обычный курс
    PRIMARY(1),

    // курс, для новодобавленных карточек, догоняет обычный
    SECONDARY(2),

    // дополнительное повторение, заланированное по желанию пользователя
    ADDITIONAL(3);

    val dbId: Int

    init {
        this.dbId = dbId
    }

    companion object {
        private val sIdToEnumMap = mutableMapOf<Int, CourseTypeEntity>()

        init {
            for (courseType in entries) {
                sIdToEnumMap.put(courseType.dbId, courseType)
            }
        }

        fun valueOfId(id: Int?): CourseTypeEntity? {
            return sIdToEnumMap.get(id)
        }
    }
}
