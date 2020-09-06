package ru.samtakoy.core.presentation.themes.mvp;

import android.net.Uri;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.database.room.entities.ThemeEntity;
import ru.samtakoy.core.presentation.log.MyLog;
import ru.samtakoy.features.import_export.QPacksExporter;
import ru.samtakoy.features.import_export.utils.ImportCardsOpts;
import ru.samtakoy.features.import_export.utils.cbuild.CBuilderConst;

import static ru.samtakoy.core.business.utils.TransformersKt.c_io_mainThread;
import static ru.samtakoy.core.business.utils.TransformersKt.s_io_mainThread;

@InjectViewState
public class ThemesListPresenter extends MvpPresenter<ThemeListView> {

    private static final int DEBOUNCE_MILLI = 1000;

    CardsInteractor mCardsInteractor;
    QPacksExporter mQPacksExporter;


    public static class Factory {

        @Inject
        CardsInteractor mCardsInteractor;
        @Inject
        QPacksExporter mQPacksExporter;

        @Inject
        public Factory() {
        }


        public ThemesListPresenter create(Long themeId) {
            return new ThemesListPresenter(
                    mCardsInteractor, mQPacksExporter, themeId
            );
        }
    }

    private List<ThemeEntity> mCurThemes;
    private List<QPackEntity> mCurQPacks;

    private ThemeEntity mParentTheme;


    private CompositeDisposable mDataGettersDisposable;
    private CompositeDisposable mOperationDisposable;

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
            Long themeId
    ) {

        mCardsInteractor = cardsInteractor;
        mQPacksExporter = qPacksExporter;

        mLastDialogState = new DialogState();

        mDataGettersDisposable = new CompositeDisposable();
        mOperationDisposable = new CompositeDisposable();

        bindTheme(themeId);

        mCurThemes = new ArrayList<>();
        mCurQPacks = new ArrayList<>();
        bindData(themeId);

    }

    @Override
    public void onDestroy() {

        mOperationDisposable.dispose();
        mDataGettersDisposable.dispose();
        super.onDestroy();
    }

    private void bindTheme(Long themeId) {

        getViewState().updateToolbarTitle(null);
        getViewState().updateToolbarSubtitle("");

        blockUiAndRunOpt(
                mCardsInteractor.getTheme(themeId)
                        .compose(s_io_mainThread())
                        .subscribe(
                                themeEntity -> {
                                    unblockUi();
                                    onParentThemeInit(themeEntity);
                                },
                                throwable -> onGetError(throwable)
                        )
        );
    }

    private void onParentThemeInit(ThemeEntity themeEntity) {

        mParentTheme = themeEntity;
        if (mParentTheme != null) {

            getViewState().updateToolbarSubtitle(themeEntity.getTitle());

            updateThemesPath();
            getViewState().updateMenuState(isExportAllMenuItemVisible(), isToBlankDbMenuItemsVisible());
        }
    }

    private void updateThemesPath() {

        if (mParentTheme.hasParent()) {
            blockUiAndRunOpt(
                    mCardsInteractor.getTheme(mParentTheme.getParentId())
                            .compose(s_io_mainThread())
                            .subscribe(
                                    themeEntity -> {
                                        unblockUi();
                                        getViewState().updateToolbarTitle("../" + themeEntity.getTitle());
                                    },
                                    throwable -> onGetError(throwable)
                            )
            );
        }
    }

    public Serializable getStateToSave() {
        return mLastDialogState;
    }

    public void onRestoreState(Serializable state) {
        mLastDialogState = (DialogState) state;
    }

    private void bindData(Long themeId) {

        mDataGettersDisposable.clear();
        mDataGettersDisposable.add(
                mCardsInteractor.getChildThemesRx(themeId)
                        .throttleLatest(DEBOUNCE_MILLI, TimeUnit.MILLISECONDS)
                        .onBackpressureLatest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(themeEntities -> {
                            mCurThemes = themeEntities;
                            setDataToView();
                        })
        );

        mDataGettersDisposable.add(
                mCardsInteractor.getChildQPacksRx(themeId)
                        .throttleLatest(DEBOUNCE_MILLI, TimeUnit.MILLISECONDS)
                        .onBackpressureLatest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(qPackEntities -> {
                            mCurQPacks = qPackEntities;
                            setDataToView();
                        })
        );

    }

    private void setDataToView() {
        // TODO через DiffUtil или сменить адаптер
        getViewState().setListData(mCurThemes, mCurQPacks);

        getViewState().updateMenuState(isExportAllMenuItemVisible(), isToBlankDbMenuItemsVisible());
    }

    private boolean isExportAllMenuItemVisible() {
        return mParentTheme == null;
    }

    private boolean isToBlankDbMenuItemsVisible() {
        return mParentTheme == null
                && mCurThemes.size() == 0
                && mCurQPacks.size() == 0;
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // from ui

    // TODO убрать и сделать через отслеживание изменений модели
    public void onUiSomeDialogClosed() {
    }

    public void onUiAddNewThemeRequest(){
        getViewState().showInputThemeTitleDialog();
    }

    public void onUiSettingsClick(){
        getViewState().navigateToSettings();
    }

    public void onUiNewThemeTitleEntered(String title) {
        blockUiAndRunOpt(
                mCardsInteractor.addNewTheme(getParentThemeId(), title)
                        .compose(s_io_mainThread())
                        .subscribe(
                                themeEntity -> unblockUi(),
                                throwable -> onGetError(throwable)
                        )
        );
    }

    private long getParentThemeId() {
        return mParentTheme == null ? CBuilderConst.NO_ID : mParentTheme.getId();
    }

    public void onUiImportPackRequest() {
        mLastDialogState.mDialogType = OPENED_DIALOG_TYPE.SELECT_FILE_TO_IMPORT;
        getViewState().showImportPackFileSelection(false);
    }

    public void onUiImportFromZipRequest(ImportCardsOpts opts) {
        mLastDialogState.mDialogType = OPENED_DIALOG_TYPE.SELECT_ZIP_TO_IMPORT;
        mLastDialogState.mImportCardOpts = opts;
        getViewState().showImportPackFileSelection(true);
    }

    public void onUiImportFileSelected(Uri selectedFileUri){
        switch (mLastDialogState.mDialogType){
            case SELECT_FILE_TO_IMPORT:
                getViewState().navigateToImportPackDialog(selectedFileUri, getParentThemeId(), ImportCardsOpts.IMPORT_ONLY_NEW);
                return;
            case SELECT_ZIP_TO_IMPORT:
                getViewState().navigateToImportFromZipDialog(selectedFileUri, mLastDialogState.mImportCardOpts);
                return;
        }


    }

    public void onUiPathSelected(String selectedPath){

        switch (mLastDialogState.mDialogType){
            case SELECT_DIR_TO_BATCH_IMPORT:
                getViewState().navigateToBatchImportFromDirDialog(selectedPath, getParentThemeId(), mLastDialogState.mImportCardOpts);
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

    //TODO больше не использую
    public void onUiExportQPackCards(QPackEntity qPack) {

        blockUiAndRunOpt(
                mQPacksExporter.exportQPack(qPack)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    unblockUi();
                                    getViewState().showMessage(R.string.fragment_themes_list_cards_export_ok);
                                },
                                throwable -> getViewState().showMessage(R.string.fragment_themes_list_cant_save_file_msg)
                        )
        );
    }

    public void onUiSendQPackCards(QPackEntity qPack) {

        blockUiAndRunOpt(
                mQPacksExporter.exportQPackToEmailRx(qPack)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    unblockUi();
                                },
                                throwable -> getViewState().showMessage(R.string.fragment_themes_list_cant_send_file_msg)
                        )
        );

    }

    public void onThemeDeleteClick(ThemeEntity theme) {

        blockUiAndRunOpt(
                mCardsInteractor.deleteTheme(theme.getId())
                        .compose(c_io_mainThread())
                        .subscribe(
                                () -> unblockUi(),
                                throwable -> onGetError(throwable)
                        )
        );
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private void onGetError(Throwable t) {
        unblockUi();
        MyLog.add(ExceptionUtils.getMessage(t), t);
        getViewState().showMessage(R.string.db_request_err_message);
    }

    private boolean isOperationInProgress() {
        return mOperationDisposable.size() > 0;
    }

    private void blockUi() {
        getViewState().blockScreenOnOperation();
    }

    private void unblockUi() {
        getViewState().unblockScreenOnOperation();
        mOperationDisposable.clear();
    }

    private void blockUiAndRunOpt(@NonNull Disposable disposable) {

        if (isOperationInProgress()) {
            MyLog.add("CardsViewPresenter: wrong ui logic");
            disposable.dispose();
            return;
        }

        blockUi();
        mOperationDisposable.clear();
        mOperationDisposable.add(disposable);
    }


}
