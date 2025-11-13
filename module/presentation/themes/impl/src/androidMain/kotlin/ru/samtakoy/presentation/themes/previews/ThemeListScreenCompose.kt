package ru.samtakoy.presentation.themes.previews

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.dropdown.getEmptyMenu
import ru.samtakoy.presentation.core.design_system.progress.ProgressOverlayUiModel
import ru.samtakoy.presentation.themes.list.ThemesListScreenInternal
import ru.samtakoy.presentation.themes.list.model.ThemeUiItem
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.getALoremIpsum

@Preview
@Composable
private fun ThemesListScreenInternal_Preview() = MyTheme {
    ThemesListScreenInternal(
        viewState = ThemeListViewModel.State(
            isLoading = false,
            progressPanel = ProgressOverlayUiModel(
                "Panel title".asA(),
                "Progress".asA()
            ),
            toolbarTitle = "title".asA(),
            toolbarSubtitle = "subtitle".asA(),
            toolbarMenu = getEmptyMenu(),
            themeContextMenu = getEmptyMenu(),
            qPackContextMenu = getEmptyMenu(),
            isExportAllMenuItemVisible = true,
            isToBlankDbMenuItemVisible = true,
            items = listOf<ThemeUiItem>(
                ThemeUiItem.Theme("1", LongUiId(1L), "theme 1".asA()),
                ThemeUiItem.Theme("2", LongUiId(2L), getALoremIpsum()),
                ThemeUiItem.Theme("3", LongUiId(3L), "theme 3".asA()),
                ThemeUiItem.QPack("4", LongUiId(4L), "qpack 4".asA(), "12-20-2024".asA(), null),
                ThemeUiItem.QPack("5", LongUiId(5L), getALoremIpsum(), "12-20-2024".asA(), "2".asA()),
            ).toImmutableList()
        ),
        onMainNavigator = {},
        onEvent = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
}