package ru.samtakoy.core.presentation.themes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuCompat
import androidx.core.view.MenuProvider
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.misc.RealPathUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.R
import ru.samtakoy.core.Const
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.export_cards.BatchExportDialogFragment
import ru.samtakoy.core.presentation.export_cards.BatchExportType
import ru.samtakoy.core.presentation.import_cards.BatchImportDialogFragment
import ru.samtakoy.core.presentation.import_cards.ImportPackDialogFragment
import ru.samtakoy.core.presentation.import_cards.ImportZipDialogFragment
import ru.samtakoy.core.presentation.log.LogActivity
import ru.samtakoy.core.presentation.qpack.info.QPackInfoFragment
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.Action
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.Event
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModel.State
import ru.samtakoy.core.presentation.themes.mv.ThemeListViewModelImpl
import ru.samtakoy.features.import_export.utils.ImportCardsOpts
import ru.samtakoy.presentation.base.observe
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner
import java.io.File
import java.lang.ref.WeakReference

class ThemesListFragment : Fragment(), ViewModelOwner {
    private var mThemesRecycler: RecyclerView? = null
    private var mThemesAdapter: ThemesAdapter? = null
    private var mRouterHolder: RouterHolder? = null

    private val viewModel: ThemeListViewModel by viewModel<ThemeListViewModelImpl> {
        parametersOf(readThemeId(), readThemeTitle())
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    private val mPermissionResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}
    private val mOpenFolderResult = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        onBatchImportFromTreeUri(it)
    }
    private val mGetContentResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            viewModel.onEvent(Event.ImportFileSelected(it))
        }
    }

    private val menuProvider = object : MenuProvider {
        private var mIsExportAllMenuItemVisible = false
        private var mIsToBlankDbMenuItemVisible = false

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_themes, menu)
            MenuCompat.setGroupDividerEnabled(menu, true)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.fragment_themes_menu_item_add -> {
                    viewModel.onEvent(Event.AddNewThemeRequest)
                    return true
                }
                R.id.menu_item_import_cards -> {
                    viewModel.onEvent(Event.ImportPackRequest)
                    return true
                }
                R.id.menu_item_import_from_folder_all -> {
                    viewModel.onEvent(Event.BatchImportRequest(ImportCardsOpts.TO_BLANK_DB_IMPORT))
                    return true
                }
                R.id.menu_item_from_folder_import_new -> {
                    viewModel.onEvent(Event.BatchImportRequest(ImportCardsOpts.IMPORT_ONLY_NEW))
                    return true
                }
                R.id.menu_item_from_folder_update_exists -> {
                    viewModel.onEvent(Event.BatchImportRequest(ImportCardsOpts.UPDATE_EXISTS_ID))
                    return true
                }
                R.id.menu_item_from_folder_import_as_new -> {
                    viewModel.onEvent(Event.BatchImportRequest(ImportCardsOpts.IMPORT_ALL_AS_NEW))
                    return true
                }
                R.id.menu_item_import_from_zip_all -> {
                    viewModel.onEvent(Event.ImportFromZipRequest(ImportCardsOpts.TO_BLANK_DB_IMPORT))
                    return true
                }
                R.id.menu_item_from_zip_import_new -> {
                    viewModel.onEvent(Event.ImportFromZipRequest(ImportCardsOpts.IMPORT_ONLY_NEW))
                    return true
                }
                R.id.menu_item_from_zip_update_exists -> {
                    viewModel.onEvent(Event.ImportFromZipRequest(ImportCardsOpts.UPDATE_EXISTS_ID))
                    return true
                }
                R.id.menu_item_from_zip_import_as_new -> {
                    viewModel.onEvent(Event.ImportFromZipRequest(ImportCardsOpts.IMPORT_ALL_AS_NEW))
                    return true
                }
                R.id.menu_item_online_import_cards -> {
                    viewModel.onEvent(Event.OnlineImportRequest)
                    return true
                }
                R.id.menu_item_export_all_to_dir -> {
                    viewModel.onEvent(Event.ExportAllToFolderRequest)
                    return true
                }
                R.id.menu_item_export_all_to_email -> {
                    viewModel.onEvent(Event.ExportAllToEmailRequest)
                    return true
                }
                R.id.menu_item_log -> {
                    // пока тут
                    startActivity(LogActivity.newActivityIntent(requireContext()))
                    return true
                }
                R.id.menu_item_settings -> {
                    viewModel.onEvent(Event.SettingsClick)
                    return true
                }
            }
            return false
        }

        override fun onPrepareMenu(menu: Menu) {
            super.onPrepareMenu(menu)

            var mi = menu.findItem(R.id.menu_item_export_all_to_dir)
            mi.isVisible = mIsExportAllMenuItemVisible
            mi = menu.findItem(R.id.menu_item_export_all_to_email)
            mi.isVisible = mIsExportAllMenuItemVisible
            mi = menu.findItem(R.id.menu_item_import_from_zip_all)
            mi.isVisible = mIsToBlankDbMenuItemVisible
            mi = menu.findItem(R.id.menu_item_from_zip)
            mi.isVisible = !mIsToBlankDbMenuItemVisible
            mi = menu.findItem(R.id.menu_item_import_from_folder_all)
            mi.isVisible = mIsToBlankDbMenuItemVisible
            mi = menu.findItem(R.id.menu_item_from_folder)
            mi.isVisible = !mIsToBlankDbMenuItemVisible
        }

        fun setIsExportAllMenuItemVisible(value: Boolean, activity: FragmentActivity) {
            if (value != mIsExportAllMenuItemVisible) {
                mIsExportAllMenuItemVisible = value
                activity.invalidateMenu()
            }
        }

        fun setIsToBlankDbMenuItemVisible(value: Boolean, activity: FragmentActivity) {
            if (value != mIsToBlankDbMenuItemVisible) {
                mIsToBlankDbMenuItemVisible = value
                activity.invalidateMenu()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_themes, container, false)
        mThemesRecycler = v.findViewById(R.id.themes_list_recycler)
        mThemesRecycler?.layoutManager = LinearLayoutManager(activity)
        mThemesAdapter = ThemesAdapter(createThemesAdapterCallback(requireActivity()))
        mThemesRecycler?.adapter = mThemesAdapter
        val fab: FloatingActionButton = v.findViewById(R.id.fab)
        mThemesRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
            }
        })
        fab.setOnClickListener { viewModel.onEvent(Event.AddNewThemeRequest) }
        registerForContextMenu(mThemesRecycler!!)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }

    private fun createThemesAdapterCallback(activity: FragmentActivity): ThemesAdapter.Callback {
        val inflaterHolder: WeakReference<FragmentActivity?> = WeakReference<FragmentActivity?>(activity)
        return object : ThemesAdapter.Callback {
            override fun getMenuInflater(): MenuInflater {
                return inflaterHolder.get()!!.menuInflater
            }

            override fun onListItemClick(item: ThemeUiItem) {
                viewModel.onEvent(Event.ListItemClick(item))
            }

            override fun onListItemLongClick(item: ThemeUiItem) {
                viewModel.onEvent(Event.ListItemLongClick(item))
            }
        }
    }

    override fun onDestroyView() {
        // requireActivity().removeMenuProvider(menuProvider)
        unregisterForContextMenu(mThemesRecycler!!)
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mRouterHolder = context as RouterHolder
    }

    override fun onDetach() {
        super.onDetach()
        mRouterHolder = null
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    private fun onAction(action: Action) {
        when (action) {
            is Action.ShowErrorMessage -> showErrorMessage(action.message)
            Action.ShowFolderSelectionDialog -> showFolderSelectionDialog()
            is Action.ShowImportPackFileSelection -> showImportPackFileSelection(action.isZip)
            Action.ShowInputThemeTitleDialog -> showInputThemeTitleDialog()
            is ThemeListViewModel.NavigationAction.NavigateToBatchExportDirDialog -> navigateToBatchExportDirDialog(
                dirPath = action.dirPath
            )
            ThemeListViewModel.NavigationAction.NavigateToBatchExportToEmailDialog -> navigateToBatchExportToEmailDialog()
            is ThemeListViewModel.NavigationAction.NavigateToBatchImportFromDirDialog -> navigateToBatchImportFromDirDialog(
                dirPath = action.dirPath,
                parentThemeId = action.parentThemeId,
                opts = action.opts
            )
            is ThemeListViewModel.NavigationAction.NavigateToImportFromZipDialog -> navigateToImportFromZipDialog(
                selectedFileUri = action.selectedFileUri,
                opts = action.opts
            )
            is ThemeListViewModel.NavigationAction.NavigateToImportPackDialog -> navigateToImportPackDialog(
                selectedFileUri = action.selectedFileUri,
                parentThemeId = action.parentThemeId,
                opts = action.opts
            )
            ThemeListViewModel.NavigationAction.NavigateToOnlineImport -> navigateToOnlineImport()
            is ThemeListViewModel.NavigationAction.NavigateToQPack -> navigateToQPack(action.qPackId)
            ThemeListViewModel.NavigationAction.NavigateToSettings -> navigateToSettings()
            is ThemeListViewModel.NavigationAction.NavigateToTheme -> navigateToTheme(
                action.themeId,
                action.themeTitle
            )
        }
    }

    private fun onViewState(state: State) {
        updateToolbarTitle(state.toolbarTitle.text)
        updateToolbarSubtitle(state.toolbarSubtitle.text)
        updateMenuState(
            isExportAllMenuItemVisible = state.isExportAllMenuItemVisible,
            isToBlankDbMenuItemVisible = state.isToBlankDbMenuItemVisible
        )
        mThemesAdapter!!.updateData(state.items)
    }

    private fun readThemeId(): Long {
        return arguments?.getLong(ARG_KEY_THEME_ID, Const.NO_PARENT_THEME_ID)
            ?: Const.NO_PARENT_THEME_ID
    }

    private fun readThemeTitle(): String {
        val args = arguments
        return if (args != null) {
            args.getString(ARG_KEY_THEME_TITLE, "")
        } else ""
    }

    private fun updateToolbarTitle(title: String?) {
        val activity = activity as AppCompatActivity?
        if (title != null) {
            activity!!.supportActionBar!!.title = title
        } else {
            activity!!.supportActionBar!!.setTitle(defaultTitleResId)
        }
    }

    private val defaultTitleResId: Int
        get() = R.string.main_drawer_themes

    private fun updateToolbarSubtitle(title: String?) {
        if (title != null) {
            val activity = activity as AppCompatActivity?
            activity!!.supportActionBar!!.subtitle = title
        }
    }

    private fun updateMenuState(isExportAllMenuItemVisible: Boolean, isToBlankDbMenuItemVisible: Boolean) {
        menuProvider.setIsExportAllMenuItemVisible(isExportAllMenuItemVisible, requireActivity())
        menuProvider.setIsToBlankDbMenuItemVisible(isToBlankDbMenuItemVisible, requireActivity())
    }

    private fun showInputThemeTitleDialog() {
        setFragmentResultListener(ThemeEditDialogFragment.REQ_KEY) { _, bundle ->
            val themeTitle = bundle.getString(ThemeEditDialogFragment.RESULT_EXTRA_TEXT)
            if (!themeTitle.isNullOrBlank()) {
                viewModel.onEvent(Event.NewThemeTitleEntered(themeTitle))
            }
        }
        val dialog = ThemeEditDialogFragment.newDialog("")
        dialog.show(parentFragmentManager, ThemeEditDialogFragment.TAG)
    }

    private fun showImportPackFileSelection(isZip: Boolean) {
        mGetContentResult.launch(if (isZip) "*/*" else "text/plain")
    }

    private fun showFolderSelectionDialog() {
        if(checkFilesReadPermission()) {
            mOpenFolderResult.launch(null)
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_send_cards -> {
                viewModel.onEvent(Event.ContextMenuItemSendCardsClick)
                return true
            }
            R.id.menu_item_delete -> {
                viewModel.onEvent(Event.ContextMenuItemDeleteClick)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    @SuppressLint("NewApi")
    private fun onBatchImportFromTreeUri(uri: Uri?) {
        val pickedDir = DocumentFile.fromTreeUri(requireContext(), uri!!)
        if (pickedDir!!.isDirectory) {
            val docUri = DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri))
            val path = RealPathUtil.getRealPath(requireContext(), docUri)!!
            viewModel.onEvent(Event.PathSelected(path))
        }
    }

    // //////////////////////////////////////////////////////////////////////////////
    // navigation

    private fun navigateToOnlineImport() {
        mRouterHolder!!.navController.navigate(R.id.action_themesListFragment_to_onlineImportFragment)
    }

    private fun navigateToSettings() {
        mRouterHolder!!.navController.navigate(R.id.settingsFragment)
    }

    private fun navigateToImportPackDialog(selectedFileUri: Uri, parentThemeId: Long, opts: ImportCardsOpts) {
        val dialog = ImportPackDialogFragment.newFragment(selectedFileUri, parentThemeId, opts)
        dialog.show(parentFragmentManager, ImportPackDialogFragment.TAG)
    }

    private fun navigateToImportFromZipDialog(selectedFileUri: Uri, opts: ImportCardsOpts) {
        val dialog = ImportZipDialogFragment.newFragment(selectedFileUri, opts)
        dialog.show(parentFragmentManager, ImportZipDialogFragment.TAG)
    }

    private fun checkDirSelected(dirPath: String): Boolean {
        val targetDir = File(dirPath)
        if (!targetDir.isDirectory) {
            Toast.makeText(context, R.string.fragment_themes_list_not_dir_msg, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun navigateToBatchImportFromDirDialog(dirPath: String, parentThemeId: Long, opts: ImportCardsOpts) {
        if (!checkDirSelected(dirPath)) {
            return
        }
        val dialog = BatchImportDialogFragment.newFragment(dirPath, parentThemeId, opts)
        dialog.show(parentFragmentManager, BatchImportDialogFragment.TAG)
    }

    private fun navigateToBatchExportDirDialog(dirPath: String) {
        if (!checkDirSelected(dirPath)) {
            return
        }
        val dialog = BatchExportDialogFragment.newQPacksFragment(
            type = BatchExportType.QPacksToDir(dirPath),
            targetFragment = this,
            targetReqCode = REQ_CODE_EXPORT_SOME_DIALOG
        )
        dialog.show(parentFragmentManager, BatchExportDialogFragment.TAG)
    }

    private fun navigateToBatchExportToEmailDialog() {
        val dialog = BatchExportDialogFragment.newQPacksFragment(
            type = BatchExportType.QPacksToEmail,
            targetFragment = this,
            targetReqCode = REQ_CODE_EXPORT_SOME_DIALOG
        )
        dialog.show(parentFragmentManager, BatchExportDialogFragment.TAG)
    }

    private fun navigateToTheme(themeId: Long, themeTitle: String) {
        mRouterHolder!!.navController.navigate(
            R.id.action_themesListFragment_self,
            buildBundle(
                themeId = themeId,
                themeTitle = themeTitle
            )
        )
    }

    private fun navigateToQPack(qPackId: Long) {
        mRouterHolder!!.navController.navigate(
            R.id.action_themesListFragment_to_qPackInfoFragment,
            QPackInfoFragment.buildBundle(qPackId)
        )
    }

    private fun checkFilesReadPermission(): Boolean {
        if (
            hasFilePermissions()
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            mPermissionResult.launch(getFilePermissions())
            return false
        }
        return true
    }

    private fun hasFilePermissions(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        getFilePermissions().forEach { permission ->
            val checkResult = ActivityCompat.checkSelfPermission(requireContext(), permission)
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // TODO переделать? SAF с Android 10
    fun getFilePermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf()
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    companion object {
        private const val ARG_KEY_THEME_ID = "ARG_KEY_THEME_ID"
        private const val ARG_KEY_THEME_TITLE = "ARG_KEY_THEME_TITLE"
        private const val REQ_CODE_EXPORT_SOME_DIALOG = 8

        private fun buildBundle(themeId: Long, themeTitle: String): Bundle {
            val args = Bundle()
            args.putLong(ARG_KEY_THEME_ID, themeId)
            args.putString(ARG_KEY_THEME_TITLE, themeTitle)
            return args
        }
    }
}