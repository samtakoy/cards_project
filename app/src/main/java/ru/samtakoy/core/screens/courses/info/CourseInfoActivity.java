package ru.samtakoy.core.screens.courses.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import ru.samtakoy.core.navigation.RouterHolder;


// TODO удалить
public class CourseInfoActivity extends AppCompatActivity implements RouterHolder {

    private static final String EXTRA_TARGET_COURSE_ID = "EXTRA_TARGET_COURSE_ID";

    public static Intent newActivityIntent(Context ctx, Long learnCourseId) {
        Intent aIntent = new Intent(ctx, CourseInfoActivity.class);
        aIntent.putExtra(EXTRA_TARGET_COURSE_ID, learnCourseId);
        return aIntent;
    }

    private Long mLearnCourseId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        mLearnCourseId = getIntent().getLongExtra(EXTRA_TARGET_COURSE_ID, 0);
        super.onCreate(savedInstanceState);
    }

    @Override
    public NavController getNavController() {
        return null;
    }


}
