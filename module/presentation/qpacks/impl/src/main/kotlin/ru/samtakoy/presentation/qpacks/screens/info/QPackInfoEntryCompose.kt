package ru.samtakoy.presentation.qpacks.screens.info

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModelImpl

@Composable
internal fun QPackInfoEntry(
    qPackId: Long,
    rootNavController: NavHostController
) {
    QPackInfoScreen(
        viewModel = koinViewModel<QPackInfoViewModelImpl> {
            parametersOf(qPackId)
        },
        onNavigationAction = { action ->
            when (action) {
                QPackInfoViewModel.NavigationAction.CloseScreen -> {
                    rootNavController.popBackStack()
                }
                is QPackInfoViewModel.NavigationAction.NavigateToCardsView -> {
                    // TODO()
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