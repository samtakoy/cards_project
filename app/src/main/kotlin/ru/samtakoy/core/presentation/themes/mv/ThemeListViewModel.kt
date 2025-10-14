package ru.samtakoy.core.presentation.themes.mv

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.themes.ThemeUiItem
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.Action
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.Event
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.State
import ru.samtakoy.features.import_export.utils.ImportCardsOpts

@Immutable
internal interface ThemeListViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val isLoading: Boolean,
        val toolbarTitle: AnnotatedString,
        val toolbarSubtitle: AnnotatedString,
        val isExportAllMenuItemVisible: Boolean,
        val isToBlankDbMenuItemVisible: Boolean,
        val items: ImmutableList<ThemeUiItem>
    )

    sealed interface Action {
        object ShowInputThemeTitleDialog : Action
        class ShowImportPackFileSelection(val isZip: Boolean) : Action
        object ShowFolderSelectionDialog : Action
        class ShowErrorMessage(val message: String) : Action
    }

    sealed interface NavigationAction : Action {
        object NavigateToOnlineImport : NavigationAction
        object NavigateToSettings : NavigationAction
        class NavigateToImportPackDialog(
            val selectedFileUri: Uri,
            val parentThemeId: Long,
            val opts: ImportCardsOpts
        ) : NavigationAction
        class NavigateToImportFromZipDialog(
            val selectedFileUri: Uri,
            val opts: ImportCardsOpts
        ) : NavigationAction
        class NavigateToBatchImportFromDirDialog(
            val dirPath: String,
            val parentThemeId: Long,
            val opts: ImportCardsOpts
        ) : NavigationAction
        class NavigateToBatchExportDirDialog(
            val dirPath: String
        ) : NavigationAction
        object NavigateToBatchExportToEmailDialog : NavigationAction
        class NavigateToTheme(val themeId: Long, val themeTitle: String) : NavigationAction
        class NavigateToQPack(val qPackId: Long) : NavigationAction
    }

    sealed interface Event {
        object AddNewThemeRequest : Event
        object SettingsClick : Event
        class NewThemeTitleEntered(val title: String) : Event
        object ImportPackRequest : Event
        class ImportFromZipRequest(val opts: ImportCardsOpts) : Event
        class ImportFileSelected(val selectedFileUri: Uri) : Event
        class PathSelected(val selectedPath: String) : Event
        class BatchImportRequest(val opts: ImportCardsOpts) : Event
        object ExportAllToFolderRequest : Event
        object ExportAllToEmailRequest : Event
        object OnlineImportRequest : Event
        class ListItemClick(val item: ThemeUiItem): Event
        class ListItemLongClick(val item: ThemeUiItem): Event
        object ContextMenuItemSendCardsClick : Event
        object ContextMenuItemDeleteClick : Event
    }
}