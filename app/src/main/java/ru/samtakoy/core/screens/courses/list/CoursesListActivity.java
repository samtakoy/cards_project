package ru.samtakoy.core.screens.courses.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.screens.SingleFragmentActivity;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;

public class CoursesListActivity extends SingleFragmentActivity implements RouterHolder {


    private static final String EXTRA_TARGET_QPACK = "EXTRA_TARGET_QPACK";
    private static final String EXTRA_TARGET_MODES = "EXTRA_TARGET_MODES";
    private static final String EXTRA_TARGET_COURSE_IDS_ARRAY = "EXTRA_TARGET_COURSE_IDS_ARRAY";


    public static Intent newActivityForModesIntent(
            Context ctx, @Nullable List<LearnCourseMode> modesShowFilter
    ){
        Intent aIntent = new Intent(ctx, CoursesListActivity.class);
        aIntent.putExtra(EXTRA_TARGET_MODES, (Serializable) modesShowFilter);
        return aIntent;
    }

    public static Intent newActivityForCourseIdsIntent(
            Context ctx, @Nullable Long[] courseIds
    ){
        Intent aIntent = new Intent(ctx, CoursesListActivity.class);
        aIntent.putExtra(EXTRA_TARGET_COURSE_IDS_ARRAY, courseIds);
        return aIntent;
    }

    @Nullable private QPack mTargetQPack;
    @Nullable private List<LearnCourseMode> mTargetModes;
    @Nullable private Long[] mTargetCourseIds;


    @Inject Router mRouter;
    @Inject NavigatorHolder mNavigatorHolder;
    private Navigator mNavigator = new SupportAppNavigator(this, getFragmentContainerId());



    @Override
    protected Fragment createFragment() {
        return CoursesListFragment.newFragment(mTargetQPack, mTargetModes, mTargetCourseIds);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate(savedInstanceState);

        mTargetQPack = (QPack)getIntent().getSerializableExtra(EXTRA_TARGET_QPACK);
        mTargetModes = (List<LearnCourseMode>)getIntent().getSerializableExtra(EXTRA_TARGET_MODES);
        mTargetCourseIds = (Long[])getIntent().getSerializableExtra(EXTRA_TARGET_COURSE_IDS_ARRAY);
    }

    @Override
    public void onResume() {
        super.onResume();
        mNavigatorHolder.setNavigator(mNavigator);
    }

    @Override
    public void onPause() {
        mNavigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public Router getRouter() {
        return mRouter;
    }
}
