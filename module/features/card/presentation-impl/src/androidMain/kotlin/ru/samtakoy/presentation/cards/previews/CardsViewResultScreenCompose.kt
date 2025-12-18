package ru.samtakoy.presentation.cards.previews

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.common.utils.di.commonUtilsModule
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.view.ViewHistoryItem
import ru.samtakoy.presentation.cards.di.cardPresentationModule
import ru.samtakoy.presentation.cards.screens.viewresult.CardsViewResultScreenInternal
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.State
import ru.samtakoy.presentation.cards.screens.viewresult.vm.mapper.CardsViewResultMapper
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import kotlin.time.ExperimentalTime

@Preview
@Composable
private fun CardsViewResultScreenInternal_Preview() = MyTheme {
    val koin = KoinApplication.init()
        .androidContext(LocalContext.current)
        .modules(
            commonUtilsModule(),
            cardPresentationModule()
        )
    val stateMapper = koin.koin.get<CardsViewResultMapper>()
    CardsViewResultScreenInternal(
        viewState = State(
            isLoading = false,
            content = runBlocking {
                @OptIn(ExperimentalTime::class)
                stateMapper.map(
                    CardViewMode.REPEATING,
                    ViewHistoryItem(
                        id = 0,
                        qPackId = 0,
                        viewedCardIds = listOf(1, 2, 3, 4, 5, 6, 7, 8),
                        todoCardIds = listOf(),
                        errorCardIds = listOf(1, 2, 3),
                        addedToFavsCardIds = listOf(3, 4, 5),
                        lastViewDate = DateUtils.currentTimeDate
                    ),
                    Schedule(emptyList())
                )
            }
        ),
        onEvent = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
}
