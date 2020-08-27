package ru.samtakoy.core.screens.courses.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import java.io.Serializable;
import java.util.List;

import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.screens.RouterHolder;


// TODO удалить
public class CoursesListActivity extends AppCompatActivity implements RouterHolder {


    private static final String EXTRA_TARGET_QPACK_ID = "EXTRA_TARGET_QPACK_ID";
    private static final String EXTRA_TARGET_MODES = "EXTRA_TARGET_MODES";
    private static final String EXTRA_TARGET_COURSE_IDS_ARRAY = "EXTRA_TARGET_COURSE_IDS_ARRAY";


    public static Intent newActivityForModesIntent(
            Context ctx, @Nullable List<LearnCourseMode> modesShowFilter
    ) {
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

    @Nullable
    private Long mTargetQPackId;
    @Nullable
    private List<LearnCourseMode> mTargetModes;
    @Nullable private Long[] mTargetCourseIds;

/*
    @Override
    protected Fragment createFragment() {
        return CoursesListFragment.newFragment(mTargetQPackId, mTargetModes, mTargetCourseIds);
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mTargetQPackId = (Long) getIntent().getSerializableExtra(EXTRA_TARGET_QPACK_ID);
        mTargetModes = (List<LearnCourseMode>) getIntent().getSerializableExtra(EXTRA_TARGET_MODES);
        mTargetCourseIds = (Long[])getIntent().getSerializableExtra(EXTRA_TARGET_COURSE_IDS_ARRAY);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mNavigatorHolder.setNavigator(mNavigator);
    }

    @Override
    public void onPause() {
        //mNavigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public NavController getNavController() {
        return null;
    }
}
