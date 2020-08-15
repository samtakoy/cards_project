package ru.samtakoy.core.business.impl;

import android.content.Context;

import java.util.List;

import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.elements.Schedule;
import ru.samtakoy.core.services.NotificationsPlannerService;

public class CoursesPlannerImpl implements CoursesPlanner {


    private Context mCtx;

    public CoursesPlannerImpl(Context context) {
        mCtx = context;
    }

    @Override
    public void reScheduleLearnCourses() {
        mCtx.startService(NotificationsPlannerService.getLearnCoursesReSchedulingIntent(
                mCtx, LearnCourseMode.REPEAT_WAITING
        ));
    }

    @Override
    public void planAdditionalCards(Long qPackId, List<Long> errorCardIds, Schedule schedule) {
        LearnCourseHelper.planAdditionalCards( mCtx, qPackId, null, errorCardIds, schedule );
    }
}
