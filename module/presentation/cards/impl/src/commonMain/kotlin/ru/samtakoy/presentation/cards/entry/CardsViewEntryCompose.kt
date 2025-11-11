package ru.samtakoy.presentation.cards.entry

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.presentation.cards.CardsViewResultRoute
import ru.samtakoy.presentation.cards.screens.view.CardsViewScreen
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModelImpl
import ru.samtakoy.presentation.cards.view.model.CardViewMode

@Composable
internal fun CardsViewEntry(
    rootNavController: NavHostController,
    viewHistoryId: Long,
    viewMode: CardViewMode
) {
    CardsViewScreen(
        viewModel = koinViewModel<CardsViewViewModelImpl> {
            parametersOf(viewHistoryId, viewMode)
        },
        onNavigationAction = { action ->
            when (action) {
                CardsViewViewModel.NavigationAction.CloseScreen -> {
                    rootNavController.popBackStack()
                }
                is CardsViewViewModel.NavigationAction.OpenResults -> {
                    rootNavController.popBackStack()
                    rootNavController.navigate(CardsViewResultRoute(action.viewHistoryItemId, action.cardViewMode))
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    )
}