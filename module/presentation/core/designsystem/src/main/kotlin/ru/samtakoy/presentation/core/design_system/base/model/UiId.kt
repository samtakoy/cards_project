package ru.samtakoy.presentation.core.design_system.base.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
interface UiId : Parcelable

@Immutable
@Parcelize
class AnyUiId : UiId

@Immutable
@Parcelize
data class LongUiId(val value: Long) : UiId

@Immutable
@Parcelize
data class StringUiId(val value: String) : UiId