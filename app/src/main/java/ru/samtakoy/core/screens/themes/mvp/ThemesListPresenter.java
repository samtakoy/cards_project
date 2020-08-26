package ru.samtakoy.core.screens.themes.mvp;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.database.room.entities.ThemeEntity;
import ru.samtakoy.features.import_export.QPacksExporter;
import ru.samtakoy.features.import_export.utils.ImportCardsOpts;

@InjectViewState
public class ThemesListPresenter extends MvpPresenter<ThemeListView> {

    CardsInteractor mCardsInteractor;
    QPacksExporter mQPacksExporter;

    private Long mThemeId;
    private String mThemeTitle;

    public static class Factory {

        @Inject
        CardsInteractor mCardsInteractor;
        @Inject
        QPacksExporter mQPacksExporter;

        @Inject
        public Factory() {
        }


        public ThemesListPresenter create(Long themeId, String themeTitle) {
            return new ThemesListPresenter(
                    mCardsInteractor, mQPacksExporter, themeId, themeTitle
            );
        }
    }

    private List<ThemeEntity> mCurThemes;
    private List<QPackEntity> mCurQPacks;

    private CompositeDisposable mCompositeDisposable;

    enum OPENED_DIALOG_TYPE implements Serializable {
        NONE,
        SELECT_DIR_TO_BATCH_IMPORT,
        SELECT_DIR_TO_BATCH_EXPORT,

        SELECT_FILE_TO_IMPORT,
        SELECT_ZIP_TO_IMPORT
    }

    private static class DialogState implements Serializable{
        OPENED_DIALOG_TYPE mDialogType;
        ImportCardsOpts mImportCardOpts;

        public DialogState() {
            mDialogType = OPENED_DIALOG_TYPE.NONE;
            mImportCardOpts = ImportCardsOpts.NONE;
        }

    }

    private DialogState mLastDialogState;

    public ThemesListPresenter(
            CardsInteractor cardsInteractor,
            QPacksExporter qPacksExporter,
            Long themeId, String themeTitle
    ) {

        mCardsInteractor = cardsInteractor;
        mQPacksExporter = qPacksExporter;


        mThemeId = themeId;
        mThemeTitle = themeTitle;

        mLastDialogState = new DialogState();

        mCompositeDisposable = new CompositeDisposable();

        updateTitles();

        mCurThemes = new ArrayList<>();
        mCurQPacks = new ArrayList<>();
        bindData();

    }

    @Override
    public void onDestroy() {

        mCompositeDisposable.dispose();
        super.onDestroy();
    }

    private void updateTitles() {

        ThemeEntity parentTheme = mCardsInteractor.getParentTheme(mThemeId);
        if (parentTheme == null) {
            getViewState().updateToolbarTitle(null);
        } else {
            getViewState().updateToolbarTitle("../" + parentTheme.getTitle());
        }

        getViewState().updateToolbarSubtitle(mThemeTitle);
    }

    public Serializable getStateToSave(){
        return mLastDialogState;
    }

    public void onRestoreState(Serializable state){
        mLastDialogState = (DialogState)state;
    }

    private void bindData() {

        mCompositeDisposable.clear();

        mCompositeDisposable.add(
                mCardsInteractor.getChildThemesRx(mThemeId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(themeEntities -> {
                            mCurThemes = themeEntities;
                            setDataToView();
                        })
        );

        mCompositeDisposable.add(
                mCardsInteractor.getChildQPacksRx(mThemeId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(qPackEntities -> {
                            mCurQPacks = qPackEntities;
                            setDataToView();
                        })
        );

    }

    private void setDataToView() {
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
    }

    public void onUiAddNewThemeRequest(){
        getViewState().showInputThemeTitleDialog();
    }

    public void onUiSettingsClick(){
        getViewState().navigateToSettings();
    }

    public void onUiNewThemeTitleEntered(String title) {
        mCardsInteractor.addNewTheme(mThemeId, title);

        // TODO проверить
        //updateListData();
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


    public void onUiExportQPackCards(QPackEntity qPack) {

        ///* TODO вернуть
        if (!mQPacksExporter.exportQPack(qPack)) {
            getViewState().showMessage(R.string.fragment_themes_list_cant_save_file_msg);
        } else {
            getViewState().showMessage(R.string.fragment_themes_list_cards_export_ok);
        }/**/
    }

    public void onUiSendQPackCards(QPackEntity qPack) {
        if (!mQPacksExporter.exportQPackToEmail(qPack)) {
            getViewState().showMessage(R.string.fragment_themes_list_cant_send_file_msg);
        }
    }

    public void onThemeDeleteClick(ThemeEntity theme) {
        if (mCardsInteractor.deleteTheme(theme.getId())) {
        }

    }


    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}
