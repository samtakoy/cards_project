package ru.samtakoy.presentation.base.model

import androidx.compose.runtime.Immutable

@Immutable
interface UiId

@Immutable
class AnyUiId : UiId

@Immutable
data class LongUiId(val value: Long) : UiId

@Immutable
data class StringUiId(val value: String) : UiId