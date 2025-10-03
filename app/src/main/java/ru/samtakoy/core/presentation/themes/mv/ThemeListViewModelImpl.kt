package ru.samtakoy.core.presentation.themes.mv

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.parcelize.Parcelize
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.core.presentation.log.MyLog
import ru.samtakoy.core.presentation.themes.ThemeUiItem
import ru.samtakoy.core.presentation.themes.mapper.ThemeUiItemMapper
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.Action
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.Event
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.NavigationAction
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.State
import ru.samtakoy.features.import_export.QPacksExporter
import ru.samtakoy.features.import_export.utils.ImportCardsOpts
import ru.samtakoy.features.import_export.utils.cbuild.CBuilderConst

internal class ThemeListViewModelImpl(
    private val cardsInteractor: CardsInteractor,
    private val qPacksExporter: QPacksExporter,
    private val uiItemsMapper: ThemeUiItemMapper,
    private val resources: Resources,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    themeId: Long,
    themeTitle: String
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        isLoading = true,
        toolbarTitle = themeTitle.asAnnotated(),
        toolbarSubtitle = "".asAnnotated(),
        isExportAllMenuItemVisible = false,
        isToBlankDbMenuItemVisible = false,
        items = emptyList<ThemeUiItem>().toImmutableList()
    )
), ThemeListViewModel {

    private var parentTheme: ThemeEntity? = null
    private var lastLongClickedItem: ThemeUiItem? = null

    private val lastDialogState = SavedStateValue<DialogState>(
        initialValueGetter = { DialogState(DialogState.Type.NONE, ImportCardsOpts.NONE) },
        keyName = KEY_DIALOG_STATE,
        savedStateHandle = savedStateHandle,
        serialize = { it },
        deserialize = { it as DialogState },
        saveScope = ioScope
    )

    init {
        bindTheme(themeId)
        bindData(themeId)
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.AddNewThemeRequest -> onUiAddNewThemeRequest()
            is Event.BatchImportRequest -> onUiBatchImportRequest(event.opts)
            Event.ExportAllToEmailRequest -> onUiExportAllToEmailRequest()
            Event.ExportAllToFolderRequest -> onUiExportAllToFolderRequest()
            is Event.ImportFileSelected -> onUiImportFileSelected(event.selectedFileUri)
            is Event.ImportFromZipRequest -> onUiImportFromZipRequest(event.opts)
            Event.ImportPackRequest -> onUiImportPackRequest()
            is Event.NewThemeTitleEntered -> onUiNewThemeTitleEntered(event.title)
            Event.OnlineImportRequest -> onUiOnlineImportRequest()
            is Event.PathSelected -> onUiPathSelected(event.selectedPath)
            Event.SettingsClick -> onUiSettingsClick()
            is Event.ListItemClick -> onUiListItemClick(event.item)
            is Event.ListItemLongClick -> onUiListItemLongClick(event.item)
            Event.ContextMenuItemDeleteClick -> onUiContextMenuItemDeleteClick()
            Event.ContextMenuItemSendCardsClick -> onUiContextMenuItemSendCardsClick()
        }
    }

    private fun onUiContextMenuItemDeleteClick() {
        val themeId: Long = (lastLongClickedItem as? ThemeUiItem.Theme)?.id?.value ?: return
        launchWithLoader(
            onError = ::onGetError
        ) {
            if(!cardsInteractor.deleteTheme(themeId)) {
                sendAction(
                    Action.ShowErrorMessage(
                        resources.getString(R.string.fragment_themes_list_cant_delete_theme_msg)
                    )
                )
            }
        }
    }

    private fun onUiContextMenuItemSendCardsClick() {
        val qPackId: Long = (lastLongClickedItem as? ThemeUiItem.QPack)?.id?.value ?: return
        launchWithLoader(
            onError = {
                MyLog.add(ExceptionUtils.getMessage(it))
                sendAction(
                    Action.ShowErrorMessage(resources.getString(R.string.fragment_themes_list_cant_send_file_msg))
                )
            }
        ) {
            qPacksExporter.exportQPackToEmail(qPackId)
        }
    }

    private fun onUiListItemClick(item: ThemeUiItem) {
        when (item) {
            is ThemeUiItem.Theme -> {
                sendAction(
                    NavigationAction.NavigateToTheme(
                        themeId = item.id.value,
                        themeTitle = item.title.text
                    )
                )
            }
            is ThemeUiItem.QPack -> {
                sendAction(
                    NavigationAction.NavigateToQPack(qPackId = item.id.value)
                )
            }
        }
    }

    private fun onUiListItemLongClick(item: ThemeUiItem) {
        lastLongClickedItem = item
    }

    private fun onUiAddNewThemeRequest() {
        sendAction(Action.ShowInputThemeTitleDialog)
    }

    private fun onUiSettingsClick() {
        sendAction(NavigationAction.NavigateToSettings)
    }

    private fun onUiNewThemeTitleEntered(title: String) {
        launchWithLoader(
            onError = ::onGetError
        ) {
            cardsInteractor.addNewTheme(getParentThemeId(), title)
        }
    }

    private fun onUiImportPackRequest() {
        lastDialogState.value = lastDialogState.value.copy(
            dialogType = DialogState.Type.SELECT_FILE_TO_IMPORT,
            importCardOpts = ImportCardsOpts.NONE
        )
        sendAction(Action.ShowImportPackFileSelection(false))
    }

    private fun onUiImportFromZipRequest(opts: ImportCardsOpts) {
        lastDialogState.value = lastDialogState.value.copy(
            dialogType = DialogState.Type.SELECT_ZIP_TO_IMPORT,
            importCardOpts = opts
        )
        sendAction(Action.ShowImportPackFileSelection(true))
    }

    private fun onUiImportFileSelected(selectedFileUri: Uri) {
        when (lastDialogState.value.dialogType) {
            DialogState.Type.SELECT_FILE_TO_IMPORT -> {
                sendAction(
                    NavigationAction.NavigateToImportPackDialog(
                        selectedFileUri,
                        getParentThemeId(),
                        ImportCardsOpts.IMPORT_ONLY_NEW
                    )
                )
            }
            DialogState.Type.SELECT_ZIP_TO_IMPORT -> {
                sendAction(
                    NavigationAction.NavigateToImportFromZipDialog(
                        selectedFileUri,
                        lastDialogState.value.importCardOpts
                    )
                )
            }
            else -> Unit
        }
    }

    private fun onUiPathSelected(selectedPath: String) {
        when (lastDialogState.value.dialogType) {
            DialogState.Type.SELECT_DIR_TO_BATCH_IMPORT -> {
                sendAction(
                    NavigationAction.NavigateToBatchImportFromDirDialog(
                        selectedPath,
                        getParentThemeId(),
                        lastDialogState.value.importCardOpts
                    )
                )
            }
            DialogState.Type.SELECT_DIR_TO_BATCH_EXPORT -> {
                sendAction(NavigationAction.NavigateToBatchExportDirDialog(selectedPath))
            }
            else -> Unit
        }
    }

    private fun onUiBatchImportRequest(opts: ImportCardsOpts) {
        lastDialogState.value = lastDialogState.value.copy(
            dialogType = DialogState.Type.SELECT_DIR_TO_BATCH_IMPORT,
            importCardOpts = opts
        )
        sendAction(Action.ShowFolderSelectionDialog)
    }

    private fun onUiExportAllToFolderRequest() {
        lastDialogState.value = lastDialogState.value.copy(
            dialogType = DialogState.Type.SELECT_DIR_TO_BATCH_EXPORT,
            importCardOpts = ImportCardsOpts.NONE
        )
        sendAction(Action.ShowFolderSelectionDialog)
    }

    private fun onUiExportAllToEmailRequest() {
        sendAction(NavigationAction.NavigateToBatchExportToEmailDialog)
    }

    private fun onUiOnlineImportRequest() {
        sendAction(NavigationAction.NavigateToOnlineImport)
    }

    private fun getParentThemeId(): Long {
        return if (parentTheme == null) CBuilderConst.NO_ID else parentTheme!!.id
    }

    private fun bindTheme(themeId: Long) {
        if (themeId > 0) {
            launchWithLoader(
                onError = {
                    viewState = viewState.copy(
                        toolbarTitle = "?".asAnnotated(),
                        toolbarSubtitle = "?".asAnnotated()
                    )
                    onGetError(it)
                }
            ) {
                parentTheme = cardsInteractor.getTheme(themeId)
                if (parentTheme != null) {
                    viewState = viewState.copy(
                        toolbarSubtitle = parentTheme?.title.orEmpty().asAnnotated()
                    )
                    updateMenuState()
                    val parentOfParent = cardsInteractor.getTheme(parentTheme!!.parentId)
                    if (parentOfParent != null) {
                        viewState = viewState.copy(
                            toolbarTitle = ("../" + parentOfParent.title).asAnnotated()
                        )
                    } else {
                        viewState = viewState.copy(
                            toolbarTitle = ".".asAnnotated()
                        )
                    }
                }
            }
        }
    }

    private fun bindData(themeId: Long) {
        combine(
            cardsInteractor.getChildThemesAsFlow(themeId)
                .debounce(DEBOUNCE_MILLI)
                .distinctUntilChanged(),
            cardsInteractor.getChildQPacksAsFlow(themeId)
                .debounce(DEBOUNCE_MILLI)
                .distinctUntilChanged()
        ) {  childThemes, childQPacks ->
            viewState = viewState.copy(
                items = (uiItemsMapper.mapThemes(childThemes) + uiItemsMapper.mapQPacks(childQPacks)).toImmutableList()
            )
            updateMenuState()
        }.launchIn(mainScope)
    }

    private fun updateMenuState() {
        viewState = viewState.copy(
            isExportAllMenuItemVisible = parentTheme == null,
            isToBlankDbMenuItemVisible = parentTheme == null && viewState.items.isEmpty()
        )
    }

    private fun onGetError(t: Throwable) {
        MyLog.add(ExceptionUtils.getMessage(t))
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.db_request_err_message))
        )
    }

    private fun launchWithLoader(
        onError: (suspend (Throwable) -> Unit)? = ::onGetError,
        block: suspend () -> Unit
    ) {
        viewState = viewState.copy(isLoading = true)
        launchCatching(
            onError = onError,
            onFinally = { viewState = viewState.copy(isLoading = false) }
        ) {
            block()
        }
    }

    @Parcelize
    private data class DialogState(
        val dialogType: Type,
        val importCardOpts: ImportCardsOpts
    ) : Parcelable {
        enum class Type {
            NONE,
            SELECT_DIR_TO_BATCH_IMPORT,
            SELECT_DIR_TO_BATCH_EXPORT,
            SELECT_FILE_TO_IMPORT,
            SELECT_ZIP_TO_IMPORT
        }
    }

    companion object {
        private const val KEY_DIALOG_STATE = "KEY_DIALOG_STATE"
        private const val DEBOUNCE_MILLI: Long = 1000
    }
}