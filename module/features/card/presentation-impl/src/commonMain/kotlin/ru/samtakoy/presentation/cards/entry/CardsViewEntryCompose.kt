package ru.samtakoy.presentation.cards.entry

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.navigation.presentation.LocalRootNavigator
import ru.samtakoy.presentation.cards.CardsViewResultParams
import ru.samtakoy.presentation.cards.CardsViewResultRoute
import ru.samtakoy.presentation.cards.screens.view.CardsViewScreen
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModelImpl
import ru.samtakoy.presentation.cards.view.model.CardViewMode

@Composable
internal fun CardsViewEntry(
    viewHistoryId: Long,
    viewMode: CardViewMode
) {
    val rootNavigator = LocalRootNavigator.current
    CardsViewScreen(
        viewModel = koinViewModel<CardsViewViewModelImpl> {
            parametersOf(viewHistoryId, viewMode)
        },
        onNavigationAction = { action ->
            when (action) {
                CardsViewViewModel.NavigationAction.CloseScreen -> {
                    rootNavigator.goBack()
                }
                is CardsViewViewModel.NavigationAction.OpenResults -> {
                    rootNavigator.replace(
                        CardsViewResultRoute(
                            CardsViewResultParams(action.viewHistoryItemId, action.cardViewMode)
                        )
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    )
}