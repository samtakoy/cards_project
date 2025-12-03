package ru.samtakoy.oldlegacy.core.presentation.courses.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId

@Immutable
data class CourseItemUiModel(
    val id: LongUiId,
    val title: AnnotatedString
)