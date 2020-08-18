package ru.samtakoy.core.screens.courses.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.screens.SingleFragmentActivity;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;

public class CourseInfoActivity extends SingleFragmentActivity implements RouterHolder {

    private static final String EXTRA_TARGET_COURSE_ID = "EXTRA_TARGET_COURSE_ID";

    public static Intent newActivityIntent(Context ctx, Long learnCourseId){
        Intent aIntent = new Intent(ctx, CourseInfoActivity.class);
        aIntent.putExtra(EXTRA_TARGET_COURSE_ID, learnCourseId);
        return aIntent;
    }

    private Long mLearnCourseId;

    @Inject Router mRouter;
    @Inject NavigatorHolder mNavigatorHolder;
    private Navigator mNavigator = new SupportAppNavigator(this, getFragmentContainerId());


    @Override
    protected Fragment createFragment() {
        return CourseInfoFragment.newFragment(mLearnCourseId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);

        mLearnCourseId = getIntent().getLongExtra(EXTRA_TARGET_COURSE_ID, 0);
        super.onCreate(savedInstanceState);
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

    @Override
    public void onBackPressed() {

        mRouter.exit();
    }
}
