package ru.samtakoy.features.learncourse.domain.model

import ru.samtakoy.features.learncourse.data.model.CourseTypeEntity

enum class CourseType {
    // обычный курс
    PRIMARY,
    // курс, для новодобавленных карточек, догоняет обычный
    SECONDARY,
    // дополнительное повторение, заланированное по желанию пользователя
    ADDITIONAL
}