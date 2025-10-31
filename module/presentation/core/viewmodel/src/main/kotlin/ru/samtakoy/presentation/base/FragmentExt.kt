package ru.samtakoy.presentation.base

import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

fun Fragment.composeContent(
    rootViewId: Int,
    block: @Composable () -> Unit
): View {
    return ComposeView(requireContext(), null, 0).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        id = rootViewId
        setContent {
            block()
        }
    }
}