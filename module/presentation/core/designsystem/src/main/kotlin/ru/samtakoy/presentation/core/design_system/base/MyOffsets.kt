package ru.samtakoy.presentation.core.design_system.base

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Временный класс, чтобы не потерять константы
 * */
object MyOffsets {
    val xxsmall: Dp = 2.dp
    val xsmall: Dp = 4.dp
    val small: Dp = 8.dp
    val medium: Dp = 16.dp
    val large: Dp = 24.dp
}

object UiOffsets {
    // Вертикальные отступы м/у элементами на экране
    val itemsSmallVOffset: Dp = MyOffsets.small
    val itemsStandartVOffset: Dp = MyOffsets.medium

    // Отступы контента экрана от краев
    val screenContentHPadding: Dp = MyOffsets.medium
    val screenContentVPadding: Dp = MyOffsets.medium

    // Отступы списковых элементов
    val listItemContentHPadding: Dp = MyOffsets.small
    val listItemContentVPadding: Dp = MyOffsets.xxsmall
    val listItemOffset: Dp = MyOffsets.small

    // Константы диалога
    /** Минимальные отступы внешней панели от краев экрана */
    val dialogSurfacePaddings: Dp = MyOffsets.medium
    /** Отступы контента от краев панели */
    val dialogContentPaddings: Dp = MyOffsets.medium
    /** Подъем панели */
    val dialogSurfaceElevation: Dp = MyOffsets.small
}