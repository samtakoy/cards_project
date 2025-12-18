package ru.samtakoy.presentation.qpacks.previews

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.dropdown.getEmptyMenu
import ru.samtakoy.presentation.qpacks.di.qPackPresentationModule
import ru.samtakoy.presentation.qpacks.screens.info.QPackInfoScreenInternal
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoButtonsMapper
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoButtonsMapper.Uncompleted
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.State
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.preview.getKoinWithModules

@Preview
@Composable
private fun QPackInfoScreenInternal_Preview() = MyTheme {
    val koin = getKoinWithModules(
        qPackPresentationModule()
    )
    val mapper = koin.get<QPackInfoButtonsMapper>()

    QPackInfoScreenInternal(
        viewState = State(
            isLoading = false,
            title = "Заголовок".asA(),
            toolbarMenu = getEmptyMenu(),
            cardsCountText = "2/3".asA(),
            isFavoriteChecked = true,
            buttons = runBlocking { mapper.map(Uncompleted(2, 10)).toImmutableList() },
            fastCards = State.CardsState.NotInit
        ),
        onEvent = { },
        snackbarHostState = remember { SnackbarHostState() }
    )
}