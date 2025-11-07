package ru.samtakoy.presentation.themes.list.vm

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.domain.exportcards.QPacksExporter
import ru.samtakoy.domain.importcards.model.ImportCardsOpts
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.task.model.TaskStateData
import ru.samtakoy.domain.theme.Theme
import ru.samtakoy.domain.theme.ThemeInteractor
import ru.samtakoy.platform.importcards.ImportCardsFromZipTask
import ru.samtakoy.platform.permissions.PermissionState
import ru.samtakoy.platform.permissions.PermissionsController
import ru.samtakoy.platform.permissions.model.MyPermission
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.inputtext.MyInputTextDialogUiModel
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel
import ru.samtakoy.presentation.core.design_system.progress.ProgressOverlayUiModel
import ru.samtakoy.presentation.themes.impl.R
import ru.samtakoy.presentation.themes.list.mapper.ThemeListMenuItemsMapper
import ru.samtakoy.presentation.themes.list.mapper.ThemeUiItemMapper
import ru.samtakoy.presentation.themes.list.model.ItemContextMenuId
import ru.samtakoy.presentation.themes.list.model.ThemeListMenuId
import ru.samtakoy.presentation.themes.list.model.ThemeUiItem
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel.Action
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel.Event
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel.NavigationAction
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel.State
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.asAnnotated

@OptIn(FlowPreview::class)
internal class ThemeListViewModelImpl(
    private val qPackInteractor: QPackInteractor,
    private val themeInteractor: ThemeInteractor,
    private val qPacksExporter: QPacksExporter,
    private val permissionsController: PermissionsController,
    private val importCardsFromZipTask: ImportCardsFromZipTask,
    private val uiItemsMapper: ThemeUiItemMapper,
    private val menuItemMapper: ThemeListMenuItemsMapper,
    private val resources: Resources,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    themeId: Long,
    themeTitle: String?
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        isLoading = false,
        progressPanel = null,
        toolbarTitle = (themeTitle ?: resources.getString(R.string.feature_themes_list_title)).asAnnotated(),
        toolbarSubtitle = "".asAnnotated(),
        toolbarMenu = menuItemMapper.mapShort(),
        themeContextMenu = menuItemMapper.mapThemeContextMenu(),
        qPackContextMenu = menuItemMapper.mapQPackContextMenu(),
        isExportAllMenuItemVisible = false,
        isToBlankDbMenuItemVisible = false,
        items = emptyList<ThemeUiItem>().toImmutableList()
    )
), ThemeListViewModel {

    private var parentTheme: Theme? = null

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
        subscribeTasksProgress()
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.AddNewThemeRequest -> onUiAddNewThemeRequest()
            is Event.ImportFileSelected -> onUiImportFileSelected(event.file)
            is Event.PathSelected -> onUiPathSelected(event.selectedPath)
            is Event.ListItemClick -> onUiListItemClick(event.item)
            is Event.ToolbarMenuItemClick -> onUiToolbarMenuItemClick(event.id)
            is Event.QPackContextMenuItemClick -> onUiQPackContextMenuItemClick(event.item, event.menuItem)
            is Event.ThemeContextMenuItemClick -> onUiThemeContextMenuItemClick(event.item, event.menuItem)
            is Event.InputDialogResult -> onUiNewThemeTitleEntered(event.title)
            is Event.AlertDialogResult -> onUiAlertDialogButtonClick(event.dialogId, event.clickedButton)
        }
    }

    private fun onUiAlertDialogButtonClick(dialogId: UiId?, clickedButton: UiId) {
        if (dialogId == ThemeUiItemMapper.NotificationsAlertDialogId) {
            if (clickedButton == ThemeUiItemMapper.OkBtnId) {
                launchCatching(onError = ::onGetError) {
                    if (permissionsController.getPermissionState(MyPermission.Notifications) == PermissionState.DeniedAlways) {
                        permissionsController.openAppSettings()
                    } else {
                        permissionsController.requestPermission(MyPermission.Notifications)
                        // запуск с нотификацией либо без
                        sendAction(Action.ShowImportPackFileSelection(isZip = true))
                    }
                }
            } else {
                // запуск задачи без нотификации
                sendAction(Action.ShowImportPackFileSelection(isZip = true))
            }
        }
    }

    private fun onUiThemeContextMenuItemClick(
        themeItem: ThemeUiItem.Theme,
        menuItem: DropDownMenuUiModel.ItemType.Item
    ) {
        if (menuItem.id == ItemContextMenuId.DeleteTheme) {
            launchWithLoader(
                onError = ::onGetError
            ) {
                if(!themeInteractor.deleteTheme(themeId = themeItem.id.value)) {
                    sendAction(
                        Action.ShowErrorMessage(
                            resources.getString(R.string.fragment_themes_list_cant_delete_theme_msg)
                        )
                    )
                }
            }
        }
    }

    private fun onUiQPackContextMenuItemClick(
        qPackItem: ThemeUiItem.QPack,
        menuItem: DropDownMenuUiModel.ItemType.Item
    ) {
        if (menuItem.id == ItemContextMenuId.ExportQPackToEmail) {
            launchWithLoader(
                onError = {
                    MyLog.add(it.stackTraceToString())
                    sendAction(
                        Action.ShowErrorMessage(resources.getString(R.string.fragment_themes_list_cant_send_file_msg))
                    )
                }
            ) {
                qPacksExporter.exportQPackToEmail(qPackId = qPackItem.id.value)
            }
        }
    }

    private fun onUiToolbarMenuItemClick(itemId: UiId) {
        when(itemId) {
            ThemeListMenuId.ImportCards -> onUiImportPackRequest()
            ThemeListMenuId.ImportFromFolderAll -> onUiBatchImportRequest(ImportCardsOpts.TO_BLANK_DB_IMPORT)
            ThemeListMenuId.FromFolderImportNew -> onUiBatchImportRequest(ImportCardsOpts.IMPORT_ONLY_NEW)
            ThemeListMenuId.FromFolderUpdateExists -> onUiBatchImportRequest(ImportCardsOpts.UPDATE_EXISTS_ID)
            ThemeListMenuId.FromFolderImportAsNew -> onUiBatchImportRequest(ImportCardsOpts.IMPORT_ALL_AS_NEW)
            ThemeListMenuId.ImportFromZipAll -> onUiImportFromZipRequest(ImportCardsOpts.TO_BLANK_DB_IMPORT)
            ThemeListMenuId.FromZipImportNew -> onUiImportFromZipRequest(ImportCardsOpts.IMPORT_ONLY_NEW)
            ThemeListMenuId.FromZipUpdateExists -> onUiImportFromZipRequest(ImportCardsOpts.UPDATE_EXISTS_ID)
            ThemeListMenuId.FromZipImportAsNew -> onUiImportFromZipRequest(ImportCardsOpts.IMPORT_ALL_AS_NEW)
            ThemeListMenuId.OnlineImportCards -> onUiOnlineImportRequest()
            ThemeListMenuId.ExportAllToDir -> onUiExportAllToFolderRequest()
            ThemeListMenuId.ExportAllToEmail -> onUiExportAllToEmailRequest()
            ThemeListMenuId.Log -> sendAction(NavigationAction.NavigateToLog)
            ThemeListMenuId.Settings -> onUiSettingsClick()
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

    private fun onUiAddNewThemeRequest() {
        sendAction(
            Action.ShowInputThemeTitleDialog(
                MyInputTextDialogUiModel(
                    id = null,
                    title = resources.getString(R.string.fragment_dialog_theme_add_title).asAnnotated(),
                    description = null,
                    inputHint = null,
                    initialText = "",
                    okButton = MyButtonUiModel(
                        id = LongUiId(0L),
                        text = resources.getString(ru.samtakoy.common.utils.R.string.action_ok).asAnnotated(),
                        isEnabled = true
                    )
                )
            )
        )
    }

    private fun onUiSettingsClick() {
        sendAction(NavigationAction.NavigateToSettings)
    }

    private fun onUiNewThemeTitleEntered(title: String) {
        if (title.isBlank()) return
        launchWithLoader(
            onError = ::onGetError
        ) {
            themeInteractor.addNewTheme(getParentThemeId(), title)
        }
    }

    private fun onUiImportPackRequest() {
        lastDialogState.value = lastDialogState.value.copy(
            dialogType = DialogState.Type.SELECT_FILE_TO_IMPORT,
            importCardOpts = ImportCardsOpts.NONE
        )
        sendAction(Action.ShowImportPackFileSelection(isZip = false))
    }

    private fun onUiImportFromZipRequest(opts: ImportCardsOpts) {
        launchWithLoader {
            lastDialogState.value = lastDialogState.value.copy(
                dialogType = DialogState.Type.SELECT_ZIP_TO_IMPORT,
                importCardOpts = opts
            )
            if (permissionsController.isPermissionGranted(MyPermission.Notifications)) {
                sendAction(Action.ShowImportPackFileSelection(isZip = true))
            } else {
                sendAction(
                    Action.ShowAlertDialog(
                        dialogModel = uiItemsMapper.mapNotificationsAlertDialog()
                    )
                )
            }
        }
    }

    private fun onUiImportFileSelected(file: PlatformFile?) {
        file ?: return
        launchWithLoader {
            when (lastDialogState.value.dialogType) {
                DialogState.Type.SELECT_FILE_TO_IMPORT -> {
                    /* TODO
                    sendAction(
                        NavigationAction.NavigateToImportPackDialog(
                            selectedFileUri,
                            getParentThemeId(),
                            ImportCardsOpts.IMPORT_ONLY_NEW
                        )
                    )
                     */
                }
                DialogState.Type.SELECT_ZIP_TO_IMPORT -> runImportCardsFromZipTask(file)
                else -> Unit
            }
        }
    }

    private suspend fun runImportCardsFromZipTask(file: PlatformFile) {
        val result = importCardsFromZipTask.import(
            zipFile = file,
            opts = lastDialogState.value.importCardOpts
        )
        if (result is TaskStateData.Error) {
            sendAction(Action.ShowErrorMessage(result.message))
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
        return if (parentTheme == null) 0L else parentTheme!!.id
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
                parentTheme = themeInteractor.getTheme(themeId)
                if (parentTheme != null) {
                    viewState = viewState.copy(
                        toolbarSubtitle = parentTheme?.title.orEmpty().asAnnotated()
                    )
                    updateMenuState()
                    val parentOfParent = themeInteractor.getTheme(parentTheme!!.parentId)
                    if (parentOfParent != null) {
                        viewState = viewState.copy(
                            toolbarTitle = ("../" + parentOfParent.title).asAnnotated()
                        )
                    } else {
                        viewState = viewState.copy(
                            toolbarTitle = resources.getString(R.string.feature_themes_list_title).asAnnotated()
                        )
                    }
                }
            }
        }
    }

    private fun bindData(themeId: Long) {
        combine(
            themeInteractor.getChildThemesAsFlow(themeId)
                .debounce(DEBOUNCE_MILLI)
                .distinctUntilChanged(),
            qPackInteractor.getChildQPacksAsFlow(themeId)
                .debounce(DEBOUNCE_MILLI)
                .distinctUntilChanged()
        ) {  childThemes, childQPacks ->
            viewState = viewState.copy(
                items = (uiItemsMapper.mapThemes(childThemes) + uiItemsMapper.mapQPacks(childQPacks))
                    .toImmutableList()
            )
            updateMenuState()
        }.launchIn(mainScope)
    }

    private fun updateMenuState() {
        val isExportAllMenuItemVisible = parentTheme == null
        val isToBlankDbMenuItemVisible = parentTheme == null && viewState.items.isEmpty()
        viewState = viewState.copy(
            isExportAllMenuItemVisible = isExportAllMenuItemVisible,
            isToBlankDbMenuItemVisible = isToBlankDbMenuItemVisible,
            toolbarMenu = menuItemMapper.map(
                isExportAllMenuItemVisible = isExportAllMenuItemVisible,
                isToBlankDbMenuItemVisible = isToBlankDbMenuItemVisible
            )
        )
    }

    private fun onGetError(t: Throwable) {
        MyLog.add(t.stackTraceToString())
        sendAction(
            Action.ShowErrorMessage(resources.getString(ru.samtakoy.common.utils.R.string.db_request_err_message))
        )
    }

    private fun subscribeTasksProgress() {
        launchCatching(
            onError = ::onGetError
        ) {
            importCardsFromZipTask
                .getStatusAsFlow()
                .onEach { state ->
                    val panelState = when (state) {
                        TaskStateData.Init -> {
                            ProgressOverlayUiModel(
                                title = resources.getString(R.string.theme_list_screen_import_from_zip_title).asA(),
                                subtitle = resources.getString(R.string.theme_list_screen_import_from_zip_init).asA()
                            )
                        }
                        is TaskStateData.ActiveStatus -> {
                            ProgressOverlayUiModel(
                                title = resources.getString(R.string.theme_list_screen_import_from_zip_title).asA(),
                                subtitle = state.message.asA()
                            )
                        }
                        else -> null
                    }
                    viewState = viewState.copy(progressPanel = panelState)
                }
                .launchIn(mainScope)
        }
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