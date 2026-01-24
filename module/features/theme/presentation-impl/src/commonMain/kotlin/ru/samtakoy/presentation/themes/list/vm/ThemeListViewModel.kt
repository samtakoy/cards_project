package ru.samtakoy.presentation.themes.list.vm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.importcards.domain.model.ImportCardsOpts
import ru.samtakoy.presentation.themes.list.model.ThemeUiItem
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel.Action
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel.Event
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel.State
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.presentation.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.alert.MyAlertDialogUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.inputtext.MyInputTextDialogUiModel
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel
import ru.samtakoy.presentation.core.design_system.progress.ProgressOverlayUiModel

@Immutable
internal interface ThemeListViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val isLoading: Boolean,
        val progressPanel: ProgressOverlayUiModel?,
        val toolbarTitle: AnnotatedString,
        val toolbarSubtitle: AnnotatedString,
        val toolbarMenu: DropDownMenuUiModel,
        val themeContextMenu: DropDownMenuUiModel,
        val qPackContextMenu: DropDownMenuUiModel,
        val isExportAllMenuItemVisible: Boolean,
        val isToBlankDbMenuItemVisible: Boolean,
        val content: Content
    ) {
        @Immutable
        sealed interface Content {
            object Init : Content
            data class Items(val items: ImmutableList<ThemeUiItem>) : Content
            data class Empty(
                val actionButton: MyButtonUiModel,
                val description: AnnotatedString
            ) : Content
        }
    }

    sealed interface Action {
        class ShowInputDialog(val dialogModel: MyInputTextDialogUiModel) : Action
        class ShowAlertDialog(val dialogModel: MyAlertDialogUiModel) : Action
        class ShowImportPackFileSelection(val isZip: Boolean) : Action
        object ShowFolderSelectionDialog : Action
        class ShowErrorMessage(val message: String) : Action
    }

    sealed interface NavigationAction : Action {
        object NavigateToOnlineImport : NavigationAction
        object NavigateToSettings : NavigationAction
        class NavigateToImportPackDialog(
            // val selectedFileUri: Uri, TODO убрал из-за недоступности Uri
            val parentThemeId: Long,
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
        object NavigateToLog : NavigationAction
    }

    sealed interface Event {
        object AddNewThemeRequest : Event
        class InputDialogResult(val dialogId: UiId?, val inputText: String) : Event
        class AlertDialogResult(val dialogId: UiId?, val clickedButton: UiId) : Event
        class ImportFileSelected(val file: PlatformFile?) : Event
        class PathSelected(val selectedPath: String) : Event
        class ListItemClick(val item: ThemeUiItem): Event
        class QPackContextMenuItemClick(
            val item: ThemeUiItem.QPack,
            val menuItem: DropDownMenuUiModel.ItemType.Item
        ) : Event
        class ThemeContextMenuItemClick(
            val item: ThemeUiItem.Theme,
            val menuItem: DropDownMenuUiModel.ItemType.Item
        ) : Event
        class ToolbarMenuItemClick(val id: UiId) : Event
        class ButtonClick(val buttonId: UiId) : Event
    }
}