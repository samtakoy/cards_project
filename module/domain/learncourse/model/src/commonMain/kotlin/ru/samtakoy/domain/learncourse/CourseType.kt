package ru.samtakoy.domain.learncourse

enum class CourseType {
    // обычный курс
    PRIMARY,
    // курс, для новодобавленных карточек, догоняет обычный
    SECONDARY,
    // дополнительное повторение, заланированное по желанию пользователя
    ADDITIONAL
}