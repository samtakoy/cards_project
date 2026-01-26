package ru.samtakoy.presentation.qpacks.screens.info

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.navigation.presentation.LocalRootNavigator
import ru.samtakoy.presentation.cards.CardsViewParams
import ru.samtakoy.presentation.cards.CardsViewRoute
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModelImpl

@Composable
internal fun QPackInfoEntry(
    qPackId: Long
) {
    val rootNavigator = LocalRootNavigator.current
    QPackInfoScreen(
        viewModel = koinViewModel<QPackInfoViewModelImpl> {
            parametersOf(qPackId)
        },
        onNavigationAction = { action ->
            when (action) {
                QPackInfoViewModel.NavigationAction.CloseScreen -> {
                    rootNavigator.goBack()
                }
                is QPackInfoViewModel.NavigationAction.NavigateToCardsView -> {
                    rootNavigator.navigate(
                        CardsViewRoute(
                            CardsViewParams(action.viewItemId, CardViewMode.LEARNING)
                        )
                    )
                }
                is QPackInfoViewModel.NavigationAction.NavigateToPackCourses -> Unit // TODO()
                is QPackInfoViewModel.NavigationAction.ShowCourseScreen -> Unit // TODO()
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    )
}