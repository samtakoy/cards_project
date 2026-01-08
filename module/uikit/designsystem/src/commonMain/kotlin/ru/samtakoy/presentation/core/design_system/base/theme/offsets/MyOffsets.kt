package ru.samtakoy.presentation.core.design_system.base.theme.offsets

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
class MyOffsets(
    // Неименованные отступы
    val xxsmall: Dp,
    val xsmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,

    // Горизонтальные отступы м/у элементами на экране
    val itemsSmallHOffset: Dp,
    // Вертикальные отступы м/у элементами на экране
    val itemsSmallVOffset: Dp,
    val itemsStandartVOffset: Dp,
    // Отступы контента экрана от краев
    val screenContentHPadding: Dp,
    val screenContentVPadding: Dp,

    // Отступы списковых элементов
    val listItemContentHPadding: Dp,
    val listItemContentVPadding: Dp,
    val listItemOffset: Dp,

    // Константы диалога
    /** Минимальные отступы внешней панели от краев экрана */
    val dialogSurfacePaddings: Dp,
    /** Отступы контента от краев панели */
    val dialogContentPaddings: Dp,
    /** Подъем панели */
    val dialogSurfaceElevation: Dp
)