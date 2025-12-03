package ru.samtakoy.oldlegacy.core.presentation.themes

import android.os.Bundle
import androidx.fragment.app.Fragment

// TODO -> Compose, оставлено временно
class ThemesListFragment : Fragment() {
    /*
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

    private fun onAction(action: Action) {
        when (action) {
            Action.ShowFolderSelectionDialog -> showFolderSelectionDialog()
            is Action.ShowImportPackFileSelection -> showImportPackFileSelection(action.isZip)

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
            ThemeListViewModel.NavigationAction.NavigateToLog -> startActivity(LogActivity.newActivityIntent(requireContext()))
        }
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

    private fun updateToolbarSubtitle(title: String?) {
        if (title != null) {
            val activity = activity as AppCompatActivity?
            activity!!.supportActionBar!!.subtitle = title
        }
    }

    private fun showImportPackFileSelection(isZip: Boolean) {
        mGetContentResult.launch(if (isZip) "*//*" else "text/plain")
    }

    private fun showFolderSelectionDialog() {
        if(checkFilesReadPermission()) {
            mOpenFolderResult.launch(null)
        }
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
    }*/

    companion object {
        private const val ARG_KEY_THEME_ID = "ARG_KEY_THEME_ID"
        private const val ARG_KEY_THEME_TITLE = "ARG_KEY_THEME_TITLE"

        private fun buildBundle(themeId: Long, themeTitle: String): Bundle {
            val args = Bundle()
            args.putLong(ARG_KEY_THEME_ID, themeId)
            args.putString(ARG_KEY_THEME_TITLE, themeTitle)
            return args
        }
    }
}