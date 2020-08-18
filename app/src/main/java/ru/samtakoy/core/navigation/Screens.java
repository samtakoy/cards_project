package ru.samtakoy.core.navigation;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.Const;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.screens.cards.CardsViewFragment;
import ru.samtakoy.core.screens.cards.types.CardViewMode;
import ru.samtakoy.core.screens.cards.types.CardViewSource;
import ru.samtakoy.core.screens.courses.info.CourseInfoFragment;
import ru.samtakoy.core.screens.courses.list.CoursesListFragment;
import ru.samtakoy.core.screens.online_import.OnlineImportActivity;
import ru.samtakoy.core.screens.qpack.QPackInfoFragment;
import ru.samtakoy.core.screens.qpacks.QPacksListFragment;
import ru.samtakoy.core.screens.themes.ThemesListFragment;
import ru.samtakoy.features.settings.SettingsFragment;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens {


    public static class ThemeListScreen extends SupportAppScreen implements TopNavigable {

        private Long mThemeId;
        private String mThemeTitle;

        public ThemeListScreen() {
            mThemeId = Const.NO_PARENT_THEME_ID;
            mThemeTitle = "";
            updateScreenKey();
        }

        public ThemeListScreen(Long themeId, String themeTitle) {
            mThemeId = themeId;
            mThemeTitle = themeTitle;
            updateScreenKey();
        }

        private void updateScreenKey() {
            screenKey = getClass().getSimpleName() + "_" + mThemeId;
        }

        @Override
        public int getTopMenuItemId() {
            return R.id.nav_packs_themes;
        }

        /*
        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return ThemesListActivity.newActivityIntent(context, mThemeId, mThemeTitle);
        }/***/

        @Nullable
        @Override
        public Fragment getFragment() {
            return ThemesListFragment.newFragment(mThemeId, mThemeTitle);
        }

    }

    public static class QPackInfoScreen extends SupportAppScreen{

        private Long mQPackId;

        public QPackInfoScreen(Long qPackId) {
            mQPackId = qPackId;
            updateScreenKey();
        }

        private void updateScreenKey() {
            screenKey = getClass().getSimpleName() + "_" + mQPackId;
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return QPackInfoFragment.createFragment(mQPackId);
        }
    }

    public static class OnlineImportScreen extends SupportAppScreen{

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return OnlineImportActivity.newActivityIntent(context);
        }
    }

    public static class SettingsScreen extends SupportAppScreen {

        @Nullable
        @Override
        public Fragment getFragment() {
            return SettingsFragment.newFragment();
        }
    }

    public static class CoursesListScreen extends SupportAppScreen implements TopNavigable {

        public static CoursesListScreen allCoursesScreen() {
            return new CoursesListScreen(null, null, null);
        }

        public static CoursesListScreen qPackCoursesScreen(@Nullable QPack targetQPack) {
            return new CoursesListScreen(targetQPack, null, null);
        }

        public static CoursesListScreen newScreenForModesIntent(@Nullable List<LearnCourseMode> modesShowFilter) {
            return new CoursesListScreen(null, modesShowFilter, null);
        }

        public static CoursesListScreen newScreenForCourseIdsIntent(@Nullable Long[] courseIds){
            return new CoursesListScreen(null, null, courseIds);
        }

        @Nullable
        private QPack mTargetQPack;
        @Nullable
        private List<LearnCourseMode> mTargetModes;
        @Nullable
        private Long[] mTargetCourseIds;

        private CoursesListScreen(
                @Nullable QPack targetQPack,
                @Nullable List<LearnCourseMode> targetModes,
                @Nullable Long[] targetCourseIds) {
            mTargetQPack = targetQPack;
            mTargetModes = targetModes;
            mTargetCourseIds = targetCourseIds;
        }

        @Override
        public int getTopMenuItemId() {
            return R.id.nav_courses;
        }

        @Nullable
        @Override
        public Fragment getFragment() {

            return CoursesListFragment.newFragment(mTargetQPack, mTargetModes, mTargetCourseIds);
        }
    }

    public static class CourseInfoScreen extends SupportAppScreen{

        private Long mLearnCourseId;

        public CourseInfoScreen(Long learnCourseId) {
            this.mLearnCourseId = learnCourseId;
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return CourseInfoFragment.newFragment(mLearnCourseId);
        }
    }

    public static class CardsViewScreen extends SupportAppScreen{

        private Long mQLearnPlanId;
        private CardViewSource mViewSource;
        private CardViewMode mViewMode;

        public CardsViewScreen(Long qLearnPlanId, CardViewSource viewSource, CardViewMode viewMode) {
            this.mQLearnPlanId = qLearnPlanId;
            this.mViewSource = viewSource;
            this.mViewMode = viewMode;
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return CardsViewFragment.newInstance(mQLearnPlanId, mViewSource, mViewMode);
        }
    }


    public static class QPacksListScreen extends SupportAppScreen implements TopNavigable {

        @Override
        public int getTopMenuItemId() {
            return R.id.nav_packs_raw_list;
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return QPacksListFragment.newInstance();
        }
    }

}
