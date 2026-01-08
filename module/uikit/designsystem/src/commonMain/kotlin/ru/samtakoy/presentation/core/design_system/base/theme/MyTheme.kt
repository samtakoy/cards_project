package ru.samtakoy.presentation.core.design_system.base.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import ru.samtakoy.presentation.core.design_system.base.theme.colors.LocalMyColors
import ru.samtakoy.presentation.core.design_system.base.theme.colors.MyColors
import ru.samtakoy.presentation.core.design_system.base.theme.offsets.LocalMyOffsets
import ru.samtakoy.presentation.core.design_system.base.theme.offsets.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.theme.radiuses.LocalMyRadiuses
import ru.samtakoy.presentation.core.design_system.base.theme.radiuses.MyRadiuses
import ru.samtakoy.presentation.core.design_system.base.theme.sizes.LocalMySizes
import ru.samtakoy.presentation.core.design_system.base.theme.sizes.MySizes

object MyTheme {
    val offsets: MyOffsets
        @ReadOnlyComposable
        @Composable
        get() = LocalMyOffsets.current

    val sizes: MySizes
        @ReadOnlyComposable
        @Composable
        get() = LocalMySizes.current

    val colors: MyColors
        @ReadOnlyComposable
        @Composable
        get() = LocalMyColors.current

    val radiuses: MyRadiuses
        @ReadOnlyComposable
        @Composable
        get() = LocalMyRadiuses.current
}