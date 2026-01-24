package ru.samtakoy.presentation.qpacks.previews

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.presentation.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.qpacks.di.qPackPresentationModule
import ru.samtakoy.presentation.qpacks.screens.list.QPackListScreenInternal
import ru.samtakoy.presentation.qpacks.screens.list.mapper.QPackListItemUiModelMapper
import ru.samtakoy.presentation.qpacks.screens.list.model.QPackSortType
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModel
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.getLoremIpsum
import ru.samtakoy.presentation.utils.preview.getKoinWithModules
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Preview
@Composable
private fun QPackListScreenPreview() = MyTheme {
    val koin = getKoinWithModules(
        qPackPresentationModule()
    )
    val mapper = koin.get<QPackListItemUiModelMapper>()
    val items: List<QPack> = remember {
        getPreviewQPackList()
    }
    QPackListScreenInternal(
        viewState = remember {
            QPackListViewModel.State(
                isLoading = false,
                title = "title".asA(),
                items = mapper.map(
                    items,
                    QPackSortType.CREATION_DATE_DESC
                ).toImmutableList(),
                scrollPoint = QPackListViewModel.LastScrollPoint(0, 0, 0),
                isFavoritesChecked = false,
                sortButton = MyButtonUiModel(AnyUiId(), "sort".asA())
            )
        },
        searchText = remember { mutableStateOf("") },
        onEvent = { },
        snackbarHostState = remember { SnackbarHostState() }
    )
}

private fun getPreviewQPackList(): List<QPack> {
    var id: Long = 0
    return listOf(
        getPreviewQPack(++id, getLoremIpsum()),
        getPreviewQPack(++id, getLoremIpsum()),
        getPreviewQPack(++id, getLoremIpsum()),
        getPreviewQPack(++id, getLoremIpsum()),
        getPreviewQPack(++id, getLoremIpsum()),
    )
}

@OptIn(ExperimentalTime::class)
private fun getPreviewQPack(id: Long, title: String): QPack = QPack(
    id = id,
    themeId = 0,
    path = "",
    fileName = "",
    title = title,
    desc = "desc",
    creationDate = Clock.System.now(),
    viewCount = 3,
    lastViewDate = Clock.System.now(),
    favorite = 1
)
