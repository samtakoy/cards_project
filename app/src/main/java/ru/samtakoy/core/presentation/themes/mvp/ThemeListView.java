package ru.samtakoy.core.presentation.themes.mvp;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.database.room.entities.ThemeEntity;
import ru.samtakoy.features.import_export.utils.ImportCardsOpts;


public interface ThemeListView extends MvpView {


    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateToolbarTitle(@Nullable String title);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateToolbarSubtitle(@Nullable String title);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setListData(List<ThemeEntity> themes, List<QPackEntity> qPacks);

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
