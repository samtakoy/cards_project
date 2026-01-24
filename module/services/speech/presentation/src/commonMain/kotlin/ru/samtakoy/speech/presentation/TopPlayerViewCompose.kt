package ru.samtakoy.speech.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.samtakoy.presentation.core.design_system.player.MyPlayerView
import ru.samtakoy.speech.presentation.vm.PlayerViewModel.Event
import ru.samtakoy.speech.presentation.vm.PlayerViewModelImpl

@Composable
fun TopPlayerView(
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<PlayerViewModelImpl>()
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    MyPlayerView(
        model = viewState.player,
        modifier = modifier,
        onControlClick = remember(viewModel) {
            { viewModel.onEvent(Event.ControlClick(it)) }
        }
    )
}