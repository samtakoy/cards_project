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
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.misc.RealPathUtil
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.samtakoy.R
import ru.samtakoy.core.Const
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.export_cards.BatchExportDialogFragment
import ru.samtakoy.core.presentation.import_cards.BatchImportDialogFragment
import ru.samtakoy.core.presentation.import_cards.ImportPackDialogFragment
import ru.samtakoy.core.presentation.import_cards.ImportZipDialogFragment
import ru.samtakoy.core.presentation.log.LogActivity
import ru.samtakoy.core.presentation.qpack.QPackInfoFragment
import ru.samtakoy.core.presentation.themes.mvp.ThemeListView
import ru.samtakoy.core.presentation.themes.mvp.ThemesListPresenter
import ru.samtakoy.features.import_export.utils.ImportCardsOpts
import java.io.File
import java.lang.ref.WeakReference
import javax.inject.Inject

class ThemesListFragment : MvpAppCompatFragment(), ThemeListView {
    private var mThemesRecycler: RecyclerView? = null
    private var mThemesAdapter: ThemesAdapter? = null
    private var mRouterHolder: RouterHolder? = null

    @InjectPresenter
    lateinit var mPresenter: ThemesListPresenter

    @Inject
    lateinit var mPresenterFactory: ThemesListPresenter.Factory
    @ProvidePresenter
    fun providePresenter(): ThemesListPresenter {
        return mPresenterFactory.create(readThemeId(), readThemeTitle())
    }

    private var mIsExportAllMenuItemVisible = false
    private var mIsToBlankDbMenuItemVisible = false

    private val mPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
    private val mOpenFolderResult = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        onBatchImportFromTreeUri(it)
    }
    private val mGetContentResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        mPresenter.onUiImportFileSelected(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mPresenter.onRestoreState(savedInstanceState.getSerializable(SAVE_KEY_PRESENTER_STATE))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVE_KEY_PRESENTER_STATE, mPresenter.stateToSave)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_themes, container, false)
        mThemesRecycler = v.findViewById(R.id.themes_list_recycler)
        mThemesRecycler?.layoutManager = LinearLayoutManager(activity)
        mThemesAdapter = ThemesAdapter(createThemesAdapterCallback())
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
        fab.setOnClickListener { mPresenter.onUiAddNewThemeRequest() }
        setHasOptionsMenu(true)
        registerForContextMenu(mThemesRecycler!!)
        return v
    }

    private fun createThemesAdapterCallback(): ThemesAdapter.Callback {
        val inflaterHolder: WeakReference<FragmentActivity?> = WeakReference<FragmentActivity?>(requireActivity())
        return object : ThemesAdapter.Callback {
            override fun getMenuInflater(): MenuInflater {
                return inflaterHolder.get()!!.menuInflater
            }

            override fun navigateToTheme(theme: ThemeEntity) {
                this@ThemesListFragment.navigateToTheme(theme)
            }

            override fun navigateToQPack(qPack: QPackEntity) {
                this@ThemesListFragment.navigateToQPack(qPack)
            }
        }
    }

    override fun onDestroyView() {
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

    private fun readThemeId(): Long {
        val args = arguments
        return args?.getLong(ARG_KEY_THEME_ID, Const.NO_PARENT_THEME_ID)
            ?: Const.NO_PARENT_THEME_ID
    }

    private fun readThemeTitle(): String {
        val args = arguments
        return if (args != null) {
            args.getString(ARG_KEY_THEME_TITLE, "")
        } else ""
    }

    override fun updateToolbarTitle(title: String?) {
        val activity = activity as AppCompatActivity?
        if (title != null) {
            activity!!.supportActionBar!!.title = title
        } else {
            activity!!.supportActionBar!!.setTitle(defaultTitleResId)
        }
    }

    private val defaultTitleResId: Int
        get() = R.string.main_drawer_themes

    override fun updateToolbarSubtitle(title: String?) {
        if (title != null) {
            val activity = activity as AppCompatActivity?
            activity!!.supportActionBar!!.subtitle = title
        }
    }

    override fun updateMenuState(isExportAllMenuItemVisible: Boolean, isToBlankDbMenuItemVisible: Boolean) {
        mIsExportAllMenuItemVisible = isExportAllMenuItemVisible
        mIsToBlankDbMenuItemVisible = isToBlankDbMenuItemVisible
        requireActivity().invalidateOptionsMenu()
    }

    override fun setListData(themes: List<ThemeEntity>, qPacks: List<QPackEntity>) {
        mThemesAdapter!!.updateData(themes, qPacks)
    }

    override fun updateList() {
        mThemesRecycler!!.adapter!!.notifyDataSetChanged()
    }

    override fun showInputThemeTitleDialog() {
        setFragmentResultListener(ThemeEditDialogFragment.REQ_KEY) { _, bundle ->
            val themeTitle = bundle.getString(ThemeEditDialogFragment.RESULT_EXTRA_TEXT)
            if (!themeTitle.isNullOrEmpty()) {
                mPresenter.onUiNewThemeTitleEntered(themeTitle)
            }
        }
        val dialog = ThemeEditDialogFragment.newDialog("")
        dialog.show(parentFragmentManager, ThemeEditDialogFragment.TAG)
    }

    override fun showImportPackFileSelection(isZip: Boolean) {
        if(checkFilesReadPermission()) {
            mGetContentResult.launch(if (isZip) "*/*" else "text/plain")
        }
    }

    override fun showFolderSelectionDialog() {
        if(checkFilesReadPermission()) {
            mOpenFolderResult.launch(null)
        }
    }

    override fun showMessage(resourceId: Int) {
        Toast.makeText(activity, resourceId, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_themes, menu)
        MenuCompat.setGroupDividerEnabled(menu, true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragment_themes_menu_item_add -> {
                mPresenter.onUiAddNewThemeRequest()
                return true
            }
            R.id.menu_item_import_cards -> {
                mPresenter.onUiImportPackRequest()
                return true
            }
            R.id.menu_item_import_from_folder_all -> {
                mPresenter.onUiBatchImportRequest(ImportCardsOpts.TO_BLANK_DB_IMPORT)
                return true
            }
            R.id.menu_item_from_folder_import_new -> {
                mPresenter.onUiBatchImportRequest(ImportCardsOpts.IMPORT_ONLY_NEW)
                return true
            }
            R.id.menu_item_from_folder_update_exists -> {
                mPresenter.onUiBatchImportRequest(ImportCardsOpts.UPDATE_EXISTS_ID)
                return true
            }
            R.id.menu_item_from_folder_import_as_new -> {
                mPresenter.onUiBatchImportRequest(ImportCardsOpts.IMPORT_ALL_AS_NEW)
                return true
            }
            R.id.menu_item_import_from_zip_all -> {
                mPresenter.onUiImportFromZipRequest(ImportCardsOpts.TO_BLANK_DB_IMPORT)
                return true
            }
            R.id.menu_item_from_zip_import_new -> {
                mPresenter.onUiImportFromZipRequest(ImportCardsOpts.IMPORT_ONLY_NEW)
                return true
            }
            R.id.menu_item_from_zip_update_exists -> {
                mPresenter.onUiImportFromZipRequest(ImportCardsOpts.UPDATE_EXISTS_ID)
                return true
            }
            R.id.menu_item_from_zip_import_as_new -> {
                mPresenter.onUiImportFromZipRequest(ImportCardsOpts.IMPORT_ALL_AS_NEW)
                return true
            }
            R.id.menu_item_online_import_cards -> {
                mPresenter.onUiOnlineImportRequest()
                return true
            }
            R.id.menu_item_export_all_to_dir -> {
                mPresenter.onUiExportAllToFolderRequest()
                return true
            }
            R.id.menu_item_export_all_to_email -> {
                mPresenter.onUiExportAllToEmailRequest()
                return true
            }
            R.id.menu_item_log -> {
                // пока тут
                startActivity(LogActivity.newActivityIntent(context))
                return true
            }
            R.id.menu_item_settings -> {
                mPresenter.onUiSettingsClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_send_cards -> {
                if (mThemesAdapter!!.isQPackLongClicked) {
                    mPresenter.onUiSendQPackCards(mThemesAdapter!!.longClickedQPack)
                }
                return true
            }
            R.id.menu_item_delete -> {
                if (!mThemesAdapter!!.isQPackLongClicked) {
                    mPresenter.onThemeDeleteClick(mThemesAdapter!!.longClickedTheme)
                }
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
            val path = RealPathUtil.getRealPath(context, docUri)
            mPresenter.onUiPathSelected(path)
        }
    }

    // //////////////////////////////////////////////////////////////////////////////
    // navigation
    override fun navigateToOnlineImport() {
        mRouterHolder!!.navController.navigate(R.id.action_themesListFragment_to_onlineImportFragment)
    }

    override fun navigateToSettings() {
        mRouterHolder!!.navController.navigate(R.id.settingsFragment)
    }

    override fun navigateToImportPackDialog(selectedFileUri: Uri, parentThemeId: Long, opts: ImportCardsOpts) {
        val dialog = ImportPackDialogFragment.newFragment(selectedFileUri, parentThemeId, opts)
        dialog.show(parentFragmentManager, ImportPackDialogFragment.TAG)
    }

    override fun navigateToImportFromZipDialog(selectedFileUri: Uri, opts: ImportCardsOpts) {
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

    override fun navigateToBatchImportFromDirDialog(dirPath: String, parentThemeId: Long, opts: ImportCardsOpts) {
        if (!checkDirSelected(dirPath)) {
            return
        }
        val dialog = BatchImportDialogFragment.newFragment(dirPath, parentThemeId, opts)
        dialog.show(parentFragmentManager, BatchImportDialogFragment.TAG)
    }

    override fun navigateToBatchExportDirDialog(dirPath: String) {
        if (!checkDirSelected(dirPath)) {
            return
        }
        val dialog = BatchExportDialogFragment.newQPacksFragment(
            dirPath, this, REQ_CODE_EXPORT_SOME_DIALOG
        )
        dialog.show(parentFragmentManager, BatchExportDialogFragment.TAG)
    }

    override fun navigateToBatchExportToEmailDialog() {
        val dialog = BatchExportDialogFragment.newQPacksFragment(
            "", this, REQ_CODE_EXPORT_SOME_DIALOG
        )
        dialog.show(parentFragmentManager, BatchExportDialogFragment.TAG)
    }

    private fun navigateToTheme(theme: ThemeEntity) {
        mRouterHolder!!.navController.navigate(R.id.action_themesListFragment_self, buildBundle(theme.id, theme.title))
    }

    private fun navigateToQPack(qPack: QPackEntity) {
        mRouterHolder!!.navController.navigate(
            R.id.action_themesListFragment_to_qPackInfoFragment,
            QPackInfoFragment.buildBundle(qPack.id)
        )
    }

    override fun blockScreenOnOperation() {}
    override fun unblockScreenOnOperation() {}

    private fun checkFilesReadPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            mPermissionResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return false
        }
        return true
    }

    companion object {
        private const val SAVE_KEY_PRESENTER_STATE = "SAVE_KEY_PRESENTER_STATE"
        private const val ARG_KEY_THEME_ID = "ARG_KEY_THEME_ID"
        private const val ARG_KEY_THEME_TITLE = "ARG_KEY_THEME_TITLE"
        private const val REQ_CODE_EXPORT_SOME_DIALOG = 8

        fun newFragment(themeId: Long, themeTitle: String): ThemesListFragment {
            val result = ThemesListFragment()
            val args = buildBundle(themeId, themeTitle)
            result.arguments = args
            return result
        }

        private fun buildBundle(themeId: Long, themeTitle: String): Bundle {
            val args = Bundle()
            args.putLong(ARG_KEY_THEME_ID, themeId)
            args.putString(ARG_KEY_THEME_TITLE, themeTitle)
            return args
        }
    }
}