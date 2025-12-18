package ru.samtakoy.presentation.cards.entry

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.presentation.cards.screens.viewresult.CardsViewResultScreen
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModelImpl
import ru.samtakoy.presentation.cards.view.model.CardViewMode

@Composable
fun CardsViewResultEntry(
    rootNavController: NavHostController,
    viewHistoryId: Long,
    viewMode: CardViewMode
) {
    CardsViewResultScreen(
        viewModel = koinViewModel<CardsViewResultViewModelImpl> {
            parametersOf(viewHistoryId, viewMode)
        },
        onNavigationAction = { action ->
            when (action) {
                is CardsViewResultViewModel.NavigationAction.CloseScreen -> {
                    rootNavController.popBackStack()
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    )
}