package ru.samtakoy.core.screens.themes.mvp;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.Theme;
import ru.samtakoy.core.services.import_utils.ImportCardsOpts;


public interface ThemeListView extends MvpView {


    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateToolbarTitle(@Nullable String title);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateToolbarSubtitle(@Nullable String title);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setListData(List<Theme> themes, List<QPack> qPacks);
    @StateStrategyType(SkipStrategy.class)
    void updateList();


    @StateStrategyType(SkipStrategy.class)
    void showInputThemeTitleDialog();

    @StateStrategyType(SkipStrategy.class)
    void showImportPackFileSelection(boolean isZip);

    @StateStrategyType(SkipStrategy.class)
    void showFolderSelectionDialog();
    @StateStrategyType(SkipStrategy.class)
    void navigateToOnlineImport();


    //@StateStrategyType(OneExecutionStateStrategy.class)
    //void navigateToAllCourses();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSettings();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToImportPackDialog(Uri selectedFileUri, Long parentThemeId, ImportCardsOpts opts);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToImportFromZipDialog(Uri selectedFileUri, ImportCardsOpts opts);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToBatchImportFromDirDialog(String dirPath, Long parentThemeId, ImportCardsOpts opts);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToBatchExportDirDialog(String dirPath);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToBatchExportToEmailDialog();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(int resourceId);

}
