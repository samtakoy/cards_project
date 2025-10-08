package ru.samtakoy.features.notifications.learn_courses

data class UncompletedTaskSettings(
    /**
     * задержка при смахивании
     */
    val shiftMillis: Int,
    /**
     * на проверку при открытии курса
     * TODO этот параметр пока решил не использовать, во всех случаях используется первый
     */
    val openCourseShiftMillis: Int
)
