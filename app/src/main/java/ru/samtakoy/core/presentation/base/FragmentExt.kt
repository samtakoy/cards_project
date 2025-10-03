package ru.samtakoy.core.presentation.base

import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import ru.samtakoy.R

fun Fragment.composeContent(
    block: @Composable () -> Unit
): View {
    return ComposeView(requireContext(), null, 0).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        id = R.id.compose_root
        setContent {
            MaterialTheme {
                block()
            }
        }
    }
}