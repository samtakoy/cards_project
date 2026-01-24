package ru.samtakoy.presentation.core.design_system.base.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.samtakoy.presentation.core.design_system.base.theme.colors.LocalMyColors
import ru.samtakoy.presentation.core.design_system.base.theme.colors.MyColors
import ru.samtakoy.presentation.core.design_system.base.theme.offsets.BaseOffsets
import ru.samtakoy.presentation.core.design_system.base.theme.offsets.DefaultBaseOffsets
import ru.samtakoy.presentation.core.design_system.base.theme.offsets.LocalMyOffsets
import ru.samtakoy.presentation.core.design_system.base.theme.offsets.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.theme.radiuses.BaseRadiuses
import ru.samtakoy.presentation.core.design_system.base.theme.radiuses.DefaultBaseRadiuses
import ru.samtakoy.presentation.core.design_system.base.theme.radiuses.LocalMyRadiuses
import ru.samtakoy.presentation.core.design_system.base.theme.radiuses.MyRadiuses
import ru.samtakoy.presentation.core.design_system.base.theme.sizes.LocalMySizes
import ru.samtakoy.presentation.core.design_system.base.theme.sizes.MySizes

@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    baseOffsets: BaseOffsets = DefaultBaseOffsets,
    baseRadiuses: BaseRadiuses = DefaultBaseRadiuses,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    val myOffsets = remember(baseOffsets) { createMyOffsets(baseOffsets) }
    val mySizes = remember { createMySizes() }
    val myColors = remember { createMyColors(colorScheme) }
    val myRadiuses = remember { createMyRadiuses(baseRadiuses) }

    CompositionLocalProvider(
        LocalMyOffsets provides myOffsets,
        LocalMySizes provides mySizes,
        LocalMyColors provides myColors,
        LocalMyRadiuses provides myRadiuses
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

private fun createMyRadiuses(baseRadiuses: BaseRadiuses): MyRadiuses {
    return MyRadiuses(
        small = baseRadiuses.r8,
        medium = baseRadiuses.r16,
        listItemBg = baseRadiuses.r8,
        innerPanelBg = baseRadiuses.r8,
        windowPanelBg = baseRadiuses.r16
    )
}

private fun createMyColors(colorScheme: ColorScheme): MyColors {
    return MyColors(
        backgroundLoaderBgColor = Color(0x22B3B3FF),
        screenBackground = colorScheme.background,
        listItemBgColor = colorScheme.surfaceVariant,
        listItemBgLightColor = colorScheme.surface,
        listItemOddBgColor = colorScheme.surface,
        listItemEvenBgColor = colorScheme.surfaceVariant,
        errorTextColor = Color(0xffB34455),
        secondTextColor = colorScheme.secondary,
        overlayColor = colorScheme.onSurface.copy(alpha = 0.32f)
    )
}

private fun createMySizes(): MySizes {
    return MySizes(
        separatorHeight = 1.dp,
        smallIcon = 12.dp
    )
}

private fun createMyOffsets(baseOffsets: BaseOffsets = DefaultBaseOffsets): MyOffsets {
    return MyOffsets(
        // Неименованные отступы
        xxsmall = baseOffsets.xxsmall,
        xsmall = baseOffsets.xsmall,
        small = baseOffsets.small,
        medium = baseOffsets.medium,
        large = baseOffsets.large,
        // Горизонтальные отступы м/у элементами на экране
        itemsSmallHOffset = baseOffsets.small,
        // Вертикальные отступы м/у элементами на экране
        itemsSmallVOffset = baseOffsets.small,
        itemsStandartVOffset = baseOffsets.medium,
        // Отступы контента экрана от краев
        screenContentHPadding = baseOffsets.medium,
        screenContentVPadding = baseOffsets.medium,
        // Отступы списковых элементов
        listItemContentHPadding = baseOffsets.small,
        listItemContentVPadding = baseOffsets.xxsmall,
        listItemOffset = baseOffsets.small,
        // Константы диалога
        /** Минимальные отступы внешней панели от краев экрана */
        dialogSurfacePaddings = baseOffsets.medium,
        /** Отступы контента от краев панели */
        dialogContentPaddings = baseOffsets.medium,
        /** Подъем панели */
        dialogSurfaceElevation = baseOffsets.small
    )
}