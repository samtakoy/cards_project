package ru.samtakoy.core.business.impl;

import android.content.Context;

import androidx.annotation.Nullable;

import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.elements.Schedule;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.presentation.log.MyLog;
import ru.samtakoy.core.services.NotificationsConst;
import ru.samtakoy.core.services.NotificationsPlannerService;

import static ru.samtakoy.core.business.utils.TransformersKt.c_io_mainThread;

public class CoursesPlannerImpl implements CoursesPlanner {


    @Inject
    Context mCtx;
    @Inject
    CoursesRepository mCoursesRepository;

    @Inject
    public CoursesPlannerImpl() {
    }

    @Override
    public void planUncompletedTasksChecking() {
        mCtx.startService(NotificationsPlannerService.getPlanUncompletedCheckingIntent(mCtx));
    }

    @Override
    public void reScheduleLearnCourses() {
        mCtx.startService(NotificationsPlannerService.getLearnCoursesReSchedulingIntent(
                mCtx, LearnCourseMode.REPEAT_WAITING
        ));
    }

    @Override
    public void planAdditionalCards(Long qPackId, List<Long> errorCardIds, Schedule schedule) {
        Completable.fromCallable(
                () -> {
                    planAdditionalCards(qPackId, null, errorCardIds, schedule);
                    return true;
                }
        )
                .compose(c_io_mainThread())
                .subscribe(() -> {
                }, throwable -> onError(throwable));
    }

    @Override
    public void planAdditionalCards(
            Long qPackId, @Nullable String subTitle, List<Long> cardIds, Schedule restSchedule
    ) {

        String title;

        if (subTitle == null) {
            title = mCtx.getResources().getString(R.string.additional_learn_course_title);
            title = MessageFormat.format(title, cardIds.size());
        } else {
            title = mCtx.getResources().getString(R.string.additional_learn_course_title_with_subtitle);
            title = MessageFormat.format(title, cardIds.size(), subTitle);
        }

        LearnCourseEntity lc = LearnCourseEntity.Companion.createNewAdditional(
                qPackId, title, cardIds, restSchedule, NotificationsConst.NEW_COURSE_LEARN_DEFAULT_MILLIS_DELTA
        );
        mCoursesRepository.addNewCourseNow(lc);

        mCtx.startService(NotificationsPlannerService.getLearnCoursesReSchedulingIntent(
                mCtx, LearnCourseMode.LEARN_WAITING
        ));
    }

    private void onError(Throwable t) {
        MyLog.add("CoursesPlannerImpl", t);
    }

}
