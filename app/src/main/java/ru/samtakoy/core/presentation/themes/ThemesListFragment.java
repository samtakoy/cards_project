package ru.samtakoy.core.presentation.themes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.misc.RealPathUtil;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.Const;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.database.room.entities.ThemeEntity;
import ru.samtakoy.core.presentation.FragmentHelperKt;
import ru.samtakoy.core.presentation.RouterHolder;
import ru.samtakoy.core.presentation.export_cards.BatchExportDialogFragment;
import ru.samtakoy.core.presentation.import_cards.BatchImportDialogFragment;
import ru.samtakoy.core.presentation.import_cards.ImportPackDialogFragment;
import ru.samtakoy.core.presentation.import_cards.ImportZipDialogFragment;
import ru.samtakoy.core.presentation.log.LogActivity;
import ru.samtakoy.core.presentation.qpack.QPackInfoFragment;
import ru.samtakoy.core.presentation.themes.mvp.ThemeListView;
import ru.samtakoy.core.presentation.themes.mvp.ThemesListPresenter;
import ru.samtakoy.features.import_export.utils.ImportCardsOpts;

public class ThemesListFragment extends MvpAppCompatFragment implements ThemeListView {

    private static final String TAG = "ThemesListFragment";


    private static final String SAVE_KEY_PRESENTER_STATE = "SAVE_KEY_PRESENTER_STATE";

    private static final String ARG_KEY_THEME_ID = "ARG_KEY_THEME_ID";
    private static final String ARG_KEY_THEME_TITLE = "ARG_KEY_THEME_TITLE";

    private static final int REQ_CODE_PERMISSIONS = 1;
    private static final int REQ_CODE_INPUT_THEME_TITLE = 2;
    private static final int REQ_CODE_OPEN_FILE_TO_IMPORT = 3;
    private static final int REQ_CODE_OPEN_DIRECTORY_TO_IMPORT = 5;
    private static final int REQ_CODE_OPEN_DIRECTORY_TO_IMPORT21 = 6;

    private static final int REQ_CODE_IMPORT_SOME_DIALOG = 7;
    private static final int REQ_CODE_EXPORT_SOME_DIALOG = 8;
    //private static final int REQ_CODE_IMPORT_PACK_DIALOG = 7;
    //private static final int REQ_CODE_BATCH_IMPORT_DIALOG = 8;
    private static final String TAG_DIALOG_ADD_THEME = "TAG_DIALOG_ADD_THEME";


    public static String getBackStackName(Long themeId){
        return ThemesListFragment.class.getName()+"_"+ themeId;
    }

    public static ThemesListFragment newFragment(Long themeId, String themeTitle) {
        ThemesListFragment result = new ThemesListFragment();
        Bundle args = buildBundle(themeId, themeTitle);
        result.setArguments(args);
        return result;
    }

    @NotNull
    private static Bundle buildBundle(Long themeId, String themeTitle) {
        Bundle args = new Bundle();
        args.putLong(ARG_KEY_THEME_ID, themeId);
        args.putString(ARG_KEY_THEME_TITLE, themeTitle);
        return args;
    }

    private RecyclerView mThemesRecycler;
    private ThemesAdapter mThemesAdapter;

    private RouterHolder mRouterHolder;

    @InjectPresenter
    ThemesListPresenter mPresenter;
    @Inject
    ThemesListPresenter.Factory mPresenterFactory;

    @ProvidePresenter
    ThemesListPresenter providePresenter() {
        return mPresenterFactory.create(readThemeId(), readThemeTitle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_themes, container, false);

        mThemesRecycler = v.findViewById(R.id.themes_list_recycler);
        mThemesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mThemesAdapter = new ThemesAdapter(createThemesAdapterCallback());
        mThemesRecycler.setAdapter(mThemesAdapter);

        final FloatingActionButton fab = v.findViewById(R.id.fab);
        mThemesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.show();
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown())
                    fab.hide();
            }
        });
        fab.setOnClickListener(view -> mPresenter.onUiAddNewThemeRequest());


        setHasOptionsMenu(true);
        registerForContextMenu(mThemesRecycler);

        return v;
    }

    private ThemesAdapter.Callback createThemesAdapterCallback() {
        return new ThemesAdapter.Callback() {

            private WeakReference<MenuInflater> mInflater = new WeakReference(getActivity().getMenuInflater());

            @NotNull
            @Override
            public MenuInflater getMenuInflater() {
                return mInflater.get();
            }

            @Override
            public void navigateToTheme(@NotNull ThemeEntity theme) {
                ThemesListFragment.this.navigateToTheme(theme);
            }

            @Override
            public void navigateToQPack(@NotNull QPackEntity qPack) {
                ThemesListFragment.this.navigateToQPack(qPack);
            }
        };
    }

    @Override
    public void onDestroyView() {

        unregisterForContextMenu(mThemesRecycler);

        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRouterHolder = (RouterHolder) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRouterHolder = null;
    }

    private Long readThemeId(){
        Bundle args = getArguments();
        if(args != null) {
            return args.getLong(ARG_KEY_THEME_ID, Const.NO_PARENT_THEME_ID);
        }
        return Const.NO_PARENT_THEME_ID;
    }

    private String readThemeTitle(){
        Bundle args = getArguments();
        if(args != null) {
            return args.getString(ARG_KEY_THEME_TITLE, "");
        }
        return "";
    }

    @Override
    public void updateToolbarTitle(@Nullable String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(title != null){
            activity.getSupportActionBar().setTitle(title);
        }else{
            activity.getSupportActionBar().setTitle(getDefaultTitleResId());
        }
    }

    private int getDefaultTitleResId() {
        return R.string.main_drawer_themes;
    }

    @Override
    public void updateToolbarSubtitle(String title) {
        if(title != null){
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.getSupportActionBar().setSubtitle(title);
        }
    }

    public void setListData(List<ThemeEntity> themes, List<QPackEntity> qPacks) {
        mThemesAdapter.updateData(themes, qPacks);

    }

    public void updateList(){
        mThemesRecycler.getAdapter().notifyDataSetChanged();
    }

    public void showInputThemeTitleDialog() {
        ThemeEditDialogFragment dialog = ThemeEditDialogFragment.newDialog("");
        dialog.setTargetFragment(this, REQ_CODE_INPUT_THEME_TITLE);
        FragmentHelperKt.showDialogFragment(dialog, this, TAG_DIALOG_ADD_THEME);
    }

    public void showImportPackFileSelection(boolean isZip) {
        fileIntent(
                REQ_CODE_OPEN_FILE_TO_IMPORT,
                //isZip ? "*/*" : "text/plain"
                //isZip ? "multipart/x-zip" : "text/plain"
                //isZip ? "application/zip" : "text/plain"
                isZip ? "*/*" : "text/plain"
                //isZip ? "application/zip, application/octet-stream" : "text/plain"
                //isZip ? "application/zip, application/octet-stream, application/x-zip-compressed, multipart/x-zip" : "text/plain"
         );
    }

    public void showFolderSelectionDialog(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            folderIntent21(REQ_CODE_OPEN_DIRECTORY_TO_IMPORT21);
        } else{
            folderIntent(REQ_CODE_OPEN_DIRECTORY_TO_IMPORT);
        }
    }

    @Override
    public void navigateToOnlineImport() {
        mRouterHolder.getNavController().navigate(R.id.action_themesListFragment_to_onlineImportFragment);
    }

    public void showMessage(int resourceId){
        Toast.makeText(getActivity(), resourceId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_themes, menu);

        MenuCompat.setGroupDividerEnabled(menu, true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem mi = menu.findItem(R.id.menu_item_export_all_to_dir);
        mi.setVisible(mPresenter.isExportAllMenuItemVisible());
        mi = menu.findItem(R.id.menu_item_export_all_to_email);
        mi.setVisible(mPresenter.isExportAllMenuItemVisible());

        boolean isToBlankDbMenuItems = mPresenter.isToBlankDbMenuItemsCalculate();

        mi = menu.findItem(R.id.menu_item_import_from_zip_all);
        mi.setVisible(isToBlankDbMenuItems);
        mi = menu.findItem(R.id.menu_item_from_zip);
        mi.setVisible(!isToBlankDbMenuItems);

        mi = menu.findItem(R.id.menu_item_import_from_folder_all);
        mi.setVisible(isToBlankDbMenuItems);
        mi = menu.findItem(R.id.menu_item_from_folder);
        mi.setVisible(!isToBlankDbMenuItems);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.fragment_themes_menu_item_add:
                mPresenter.onUiAddNewThemeRequest();
                return true;
            case R.id.menu_item_import_cards:
                mPresenter.onUiImportPackRequest();
                return true;


            case R.id.menu_item_import_from_folder_all:
                mPresenter.onUiBatchImportRequest(ImportCardsOpts.TO_BLANK_DB_IMPORT);
                return true;
            case R.id.menu_item_from_folder_import_new:
                mPresenter.onUiBatchImportRequest(ImportCardsOpts.IMPORT_ONLY_NEW);
                return true;
            case R.id.menu_item_from_folder_update_exists:
                mPresenter.onUiBatchImportRequest(ImportCardsOpts.UPDATE_EXISTS_ID);
                return true;
            case R.id.menu_item_from_folder_import_as_new:
                mPresenter.onUiBatchImportRequest(ImportCardsOpts.IMPORT_ALL_AS_NEW);
                return true;

            case R.id.menu_item_import_from_zip_all:
                mPresenter.onUiImportFromZipRequest(ImportCardsOpts.TO_BLANK_DB_IMPORT);
                return true;
            case R.id.menu_item_from_zip_import_new:
                mPresenter.onUiImportFromZipRequest(ImportCardsOpts.IMPORT_ONLY_NEW);
                return true;
            case R.id.menu_item_from_zip_update_exists:
                mPresenter.onUiImportFromZipRequest(ImportCardsOpts.UPDATE_EXISTS_ID);
                return true;
            case R.id.menu_item_from_zip_import_as_new:
                mPresenter.onUiImportFromZipRequest(ImportCardsOpts.IMPORT_ALL_AS_NEW);
                return true;

            case R.id.menu_item_online_import_cards:
                mPresenter.onUiOnlineImportRequest();
                return true;
            case R.id.menu_item_export_all_to_dir:
                mPresenter.onUiExportAllToFolderRequest();
                return true;
            case R.id.menu_item_export_all_to_email:
                mPresenter.onUiExportAllToEmailRequest();
                return true;
            case R.id.menu_item_log:
                // пока тут
                startActivity(LogActivity.newActivityIntent(getContext()));
                return true;

            case R.id.menu_item_settings:
                mPresenter.onUiSettingsClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_item_send_cards:
                if(mThemesAdapter.isQPackLongClicked()){
                    mPresenter.onUiSendQPackCards(mThemesAdapter.getLongClickedQPack());
                }else{}

                return true;

            case R.id.menu_item_delete:
                if(mThemesAdapter.isQPackLongClicked()){
                }else{
                    mPresenter.onThemeDeleteClick(mThemesAdapter.getLongClickedTheme());
                }

                return true;

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case REQ_CODE_INPUT_THEME_TITLE:
                String themeTitle = data.getStringExtra(ThemeEditDialogFragment.RESULT_EXTRA_TEXT);
                if(themeTitle != null && !themeTitle.isEmpty()){
                    mPresenter.onUiNewThemeTitleEntered(themeTitle);
                }
                break;
            case REQ_CODE_OPEN_FILE_TO_IMPORT:
                if(data != null){
                    mPresenter.onUiImportFileSelected(data.getData());
                }
                break;
            case REQ_CODE_OPEN_DIRECTORY_TO_IMPORT:
                if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    mPresenter.onUiPathSelected(data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
                }
                break;
            case REQ_CODE_OPEN_DIRECTORY_TO_IMPORT21:
                if(data != null){
                    Uri uri = data.getData();
                    onBatchImportFromTreeUri(uri);
                }
                break;
            case REQ_CODE_IMPORT_SOME_DIALOG:
                //case REQ_CODE_IMPORT_PACK_DIALOG:
            //case REQ_CODE_BATCH_IMPORT_DIALOG:
                if(resultCode == Activity.RESULT_OK){
                    mPresenter.onUiSomeDialogClosed();
                }
                break;

            case REQ_CODE_EXPORT_SOME_DIALOG:
                // DO nothing
                break;
        }
    }

    @SuppressLint("NewApi")
    private void onBatchImportFromTreeUri(Uri uri) {
        DocumentFile pickedDir = DocumentFile.fromTreeUri(getContext(), uri);
        if(pickedDir.isDirectory()){
            Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
            String path = RealPathUtil.getRealPath(getContext(), docUri);
            mPresenter.onUiPathSelected(path);
        }
    }

    private boolean isSamsung() {
        return Build.MANUFACTURER.toLowerCase().contains("samsung");
    }

    private boolean checkFilesReadPermission(){
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_CODE_PERMISSIONS);
            return  false;
        }
        return true;
    }

    private void fileIntent(int reqCode, String MIME_TYPE){

        //final String MIME_TYPE = "text/plain";
        //final String MIME_TYPE = "*/*";
        //final String MIME_TYPE = "file/*";

        if(checkFilesReadPermission()) {
            if(isSamsung()){
                selectFileOrFolderOnSamsung(reqCode, MIME_TYPE);
            } else {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType(MIME_TYPE);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(i, "Select File To Import"), reqCode);
            }
        }
    }

    private void folderIntent21(int reqCode){

        if(checkFilesReadPermission()) {
            if(isSamsung()){
                selectFileOrFolderOnSamsung(reqCode, "file/*");
            } else {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(i, reqCode);
            }
        }
    }

    private void folderIntent(int reqCode){

        final Intent chooserIntent = new Intent(getContext(), DirectoryChooserActivity.class);
        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName("new directory")
                .allowReadOnlyDirectory(true)
                .allowNewDirectoryNameModification(true)
                //.TODO
                .build();
        chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
        startActivityForResult(chooserIntent, reqCode);
    }

    private void selectFileOrFolderOnSamsung(int reqCode, String MIME_TYPE) {
        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        intent.putExtra("CONTENT_TYPE", MIME_TYPE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, reqCode);
    }


    // //////////////////////////////////////////////////////////////////////////////

    @Override
    public void navigateToSettings() {

        mRouterHolder.getNavController().navigate(R.id.settingsFragment);

        //mRouterHolder.getRouter().navigateTo(new Screens.SettingsScreen());
    }

    @Override
    public void navigateToImportPackDialog(Uri selectedFileUri, Long parentThemeId, ImportCardsOpts opts) {
        ImportPackDialogFragment dialog = ImportPackDialogFragment.newFragment(
                selectedFileUri, parentThemeId, opts,
                this, REQ_CODE_IMPORT_SOME_DIALOG
        );
        FragmentHelperKt.showDialogFragment(dialog, this, ImportPackDialogFragment.TAG);
    }


    @Override
    public void navigateToImportFromZipDialog(Uri selectedFileUri, ImportCardsOpts opts) {
        ImportZipDialogFragment dialog = ImportZipDialogFragment.newFragment(
                selectedFileUri,
                opts,
                this, REQ_CODE_IMPORT_SOME_DIALOG
        );
        FragmentHelperKt.showDialogFragment(dialog, this, ImportZipDialogFragment.TAG);
    }

    private boolean checkDirSelected(String dirPath){
        File targetDir = new File(dirPath);
        if(!targetDir.isDirectory()){
            Toast.makeText(getContext(), R.string.fragment_themes_list_not_dir_msg, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void navigateToBatchImportFromDirDialog(String dirPath, Long parentThemeId, ImportCardsOpts opts){

        if(!checkDirSelected(dirPath)){ return; }

        BatchImportDialogFragment dialog = BatchImportDialogFragment.newFragment(
                dirPath, parentThemeId, opts, this, REQ_CODE_IMPORT_SOME_DIALOG
        );
        FragmentHelperKt.showDialogFragment(dialog, this, BatchImportDialogFragment.TAG);
    }

    @Override
    public void navigateToBatchExportDirDialog(String dirPath) {

        if(!checkDirSelected(dirPath)){ return; }

        BatchExportDialogFragment dialog = BatchExportDialogFragment.newQPacksFragment(
                dirPath, this, REQ_CODE_EXPORT_SOME_DIALOG
        );
        FragmentHelperKt.showDialogFragment(dialog, this, BatchImportDialogFragment.TAG);
    }

    @Override
    public void navigateToBatchExportToEmailDialog() {
        BatchExportDialogFragment dialog = BatchExportDialogFragment.newQPacksFragment(
                "", this, REQ_CODE_EXPORT_SOME_DIALOG
        );
        FragmentHelperKt.showDialogFragment(dialog, this, BatchImportDialogFragment.TAG);
    }

    private void navigateToTheme(ThemeEntity theme) {
        mRouterHolder.getNavController().navigate(R.id.action_themesListFragment_self, buildBundle(theme.getId(), theme.getTitle()));
    }

    private void navigateToQPack(QPackEntity qPack) {
        mRouterHolder.getNavController().navigate(
                R.id.action_themesListFragment_to_qPackInfoFragment,
                QPackInfoFragment.buildBundle(qPack.getId())
        );
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPresenter.onRestoreState(savedInstanceState.getSerializable(SAVE_KEY_PRESENTER_STATE));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_KEY_PRESENTER_STATE, mPresenter.getStateToSave());
    }





}
