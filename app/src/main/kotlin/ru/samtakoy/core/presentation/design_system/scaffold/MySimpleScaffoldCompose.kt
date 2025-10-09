package ru.samtakoy.core.presentation.design_system.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.samtakoy.core.presentation.design_system.base.MyColors
import ru.samtakoy.core.presentation.design_system.loader.MyLoaderContainer

@Composable
fun MySimpleScreenScaffold(
    isLoaderVisible: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(color = MyColors.getScreenBackground()),
    ) {
        content()
        SnackbarHost(snackbarHostState)
        MyLoaderContainer(
            isVisible = isLoaderVisible,
            modifier = Modifier.matchParentSize()
        )
    }
}