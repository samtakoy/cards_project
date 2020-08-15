package ru.samtakoy.core.business.impl;

import android.content.Context;

import androidx.annotation.Nullable;

import java.sql.Date;
import java.text.MessageFormat;
import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.elements.Schedule;
import ru.samtakoy.core.services.NotificationsConst;
import ru.samtakoy.core.services.NotificationsPlannerService;

// все сохранения по прогрессу обучения/тренировок тут
// TODO не забывать отмечать просматриваемые карты персонально
public class LearnCourseHelper {

    public static Long[] getLearnCourseIds(List<LearnCourse> list){
        Long[] result = new Long[list.size()];
        int i = 0;
        for(LearnCourse learnCourse:list){
            result[i++] = learnCourse.getId();
        }
        return result;
    }

    public static void planAdditionalCards(
            Context ctx, Long qPackId,
            @Nullable String subTitle,
            List<Long> cardIds, Schedule restSchedule
    ) {

        String title;

        if(subTitle == null){
            title = ctx.getResources().getString(R.string.additional_learn_course_title);
            title = MessageFormat.format(title, cardIds.size());
        } else {
            title = ctx.getResources().getString(R.string.additional_learn_course_title_with_subtitle);
            title = MessageFormat.format(title, cardIds.size(), subTitle);
        }

        LearnCourse lc = LearnCourse.createNewAdditional(qPackId, title, cardIds, restSchedule, NotificationsConst.NEW_COURSE_LEARN_DEFAULT_MILLIS_DELTA);
        ContentProviderHelper.addNewCourse(ctx, lc);

        ctx.startService(NotificationsPlannerService.getLearnCoursesReSchedulingIntent(
                ctx, LearnCourseMode.LEARN_WAITING
        ));
    }

    public static Long addNewCourse(
            Context ctx, Long qPackId, String title,
            @Nullable List<Long> cardIds, @Nullable Schedule restSchedule, @Nullable Date repeatDate
    ) {
        LearnCourse lc = LearnCourse.createNewPreparing(
                qPackId, title, LearnCourseMode.PREPARING, cardIds, restSchedule, repeatDate
        );
        return ContentProviderHelper.addNewCourse(ctx, lc);
    }

}