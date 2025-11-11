package ru.samtakoy.presentation.core.design_system.button.round

import androidx.compose.runtime.Immutable
import ru.samtakoy.presentation.core.design_system.base.model.UiId

@Immutable
data class MyFabButtonUiModel(
    val id: UiId,
    val icon: MyFabButtonIcon
)

enum class MyFabButtonIcon {
    Revert
}