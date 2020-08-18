package ru.samtakoy.core.screens.themes.mvp;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.QPacksExporter;
import ru.samtakoy.core.di.components.AppComponent;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.Theme;
import ru.samtakoy.core.services.import_utils.ImportCardsOpts;

@InjectViewState
public class ThemesListPresenter extends MvpPresenter<ThemeListView> {

    @Inject CardsInteractor mCardsInteractor;
    @Inject QPacksExporter mQPacksExporter;

    private Long mThemeId;
    private String mThemeTitle;


    private List<Theme> mCurThemes;
    private List<QPack> mCurQPacks;

    enum OPENED_DIALOG_TYPE implements Serializable{
        NONE,
        SELECT_DIR_TO_BATCH_IMPORT,
        SELECT_DIR_TO_BATCH_EXPORT,

        SELECT_FILE_TO_IMPORT,
        SELECT_ZIP_TO_IMPORT
    }

    private static class DialogState implements Serializable{
        OPENED_DIALOG_TYPE mDialogType;
        ImportCardsOpts mImportCardOpts;

        public DialogState(){
            mDialogType = OPENED_DIALOG_TYPE.NONE;
            mImportCardOpts = ImportCardsOpts.NONE;
        }

    }

    // Caused by: java.io.NotSerializableException: ru.samtakoy.core.screens.themes.mvp.ThemesListPresenter

    private DialogState mLastDialogState;


    public ThemesListPresenter(AppComponent appComponent, Long themeId, String themeTitle){


        appComponent.inject(this);


        mThemeId = themeId;
        mThemeTitle = themeTitle;

        mLastDialogState = new DialogState();


        updateTitles();
        updateListData();
    }

    private void updateTitles() {

        Theme parentTheme = mCardsInteractor.getParentTheme(mThemeId);
        if(parentTheme == null){
            getViewState().updateToolbarTitle(null);
        }else{
            getViewState().updateToolbarTitle("../"+parentTheme.getTitle());
        }

        getViewState().updateToolbarSubtitle(mThemeTitle);
    }

    public Serializable getStateToSave(){
        return mLastDialogState;
    }

    public void onRestoreState(Serializable state){
        mLastDialogState = (DialogState)state;
    }

    private void updateCurThemes() {
        mCurThemes = mCardsInteractor.getChildThemes(mThemeId);
    }

    private void updateCurQPacks() {
        mCurQPacks = mCardsInteractor.getChildQPacks(mThemeId);
    }

    private void updateListData() {

        updateCurThemes();
        updateCurQPacks();

        // TODO через DiffUtil


        getViewState().setListData(mCurThemes, mCurQPacks);
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // menu

    public boolean isExportAllMenuItemVisible(){
        return  mThemeId <= 0;
    }

    public boolean isToBlankDbMenuItemsCalculate(){
        return !mCardsInteractor.hasAnyQPack();
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // from ui

    // TODO убрать и сделать через отслеживание изменений модели
    public void onUiSomeDialogClosed(){
        updateListData();
    }

    public void onUiAddNewThemeRequest(){
        getViewState().showInputThemeTitleDialog();
    }

    /*
    public void onUiAllCoursesClick(){
        getViewState().navigateToAllCourses();
    }/***/

    public void onUiSettingsClick(){
        getViewState().navigateToSettings();
    }

    public void onUiNewThemeTitleEntered(String title){
        mCardsInteractor.addNewTheme(mThemeId, title);
        updateListData();
    }

    public void onUiImportPackRequest(){
        mLastDialogState.mDialogType = OPENED_DIALOG_TYPE.SELECT_FILE_TO_IMPORT;
        getViewState().showImportPackFileSelection(false);
    }

    public void onUiImportFromZipRequest(ImportCardsOpts opts){
        mLastDialogState.mDialogType = OPENED_DIALOG_TYPE.SELECT_ZIP_TO_IMPORT;
        mLastDialogState.mImportCardOpts = opts;
        getViewState().showImportPackFileSelection(true);
    }

    public void onUiImportFileSelected(Uri selectedFileUri){


        switch (mLastDialogState.mDialogType){
            case SELECT_FILE_TO_IMPORT:
                getViewState().navigateToImportPackDialog(selectedFileUri, mThemeId, ImportCardsOpts.IMPORT_ONLY_NEW);
                return;
            case SELECT_ZIP_TO_IMPORT:
                getViewState().navigateToImportFromZipDialog(selectedFileUri, mLastDialogState.mImportCardOpts);
                return;
        }


    }

    public void onUiPathSelected(String selectedPath){

        switch (mLastDialogState.mDialogType){
            case SELECT_DIR_TO_BATCH_IMPORT:
                getViewState().navigateToBatchImportFromDirDialog(selectedPath, mThemeId, mLastDialogState.mImportCardOpts);
                return;
            case SELECT_DIR_TO_BATCH_EXPORT:
                // export all path!
                getViewState().navigateToBatchExportDirDialog(selectedPath);
                return;
        }
    }

    public void onUiBatchImportRequest(ImportCardsOpts opts){
        mLastDialogState.mDialogType = OPENED_DIALOG_TYPE.SELECT_DIR_TO_BATCH_IMPORT;
        mLastDialogState.mImportCardOpts = opts;
        getViewState().showFolderSelectionDialog();
    }

    public void onUiExportAllToFolderRequest(){
        mLastDialogState.mDialogType = OPENED_DIALOG_TYPE.SELECT_DIR_TO_BATCH_EXPORT;
        getViewState().showFolderSelectionDialog();
    }

    public void onUiExportAllToEmailRequest(){
        // export all path!
        getViewState().navigateToBatchExportToEmailDialog();
    }

    public void onUiOnlineImportRequest(){
        getViewState().navigateToOnlineImport();
    }


    public void onUiExportQPackCards(QPack qPack) {

        //mQPacksExporter.exportThemeTree(qPack);
        //getViewState().showMessage(R.string.fragment_themes_list_cards_export_ok);

        ///* TODO вернуть
        if(!mQPacksExporter.exportQPack(qPack)){
            getViewState().showMessage(R.string.fragment_themes_list_cant_save_file_msg);
        } else {
            getViewState().showMessage(R.string.fragment_themes_list_cards_export_ok);
        }/**/
    }

    public void onUiSendQPackCards(QPack qPack) {
        if(!mQPacksExporter.exportQPackToEmail(qPack)){
            getViewState().showMessage(R.string.fragment_themes_list_cant_send_file_msg);
        }
    }

    public void onThemeDeleteClick(Theme theme) {
        if(mCardsInteractor.deleteTheme(theme.getId())){
            updateListData();
        }

    }


    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}
