package ru.samtakoy.core.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.other.LearnCourseHelper;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.screens.courses.info.CourseInfoActivity;
import ru.samtakoy.core.screens.courses.list.CoursesListActivity;
import ru.samtakoy.core.screens.log.MyLog;
import ru.samtakoy.core.services.learn_courses.LearnsApi;
import ru.samtakoy.core.services.learn_courses.NewRepeatsApi;
import ru.samtakoy.core.services.learn_courses.UncompletedTaskApi;


public class NotificationsPlannerService extends IntentService {

    // типизирование интента
    private static final String EXTRA_FLAG = "EXTRA_FLAG";
    private static final String EXTRA_TARGET_MODE = "EXTRA_TARGET_MODE";
    //private static final String EXTRA_ON_BOOT = "EXTRA_ON_BOOT";

    // перепланировать все
    private static final int FLAG_ON_BOOT_RESCHEDULING = 1;

    private static final int FLAG_NEW_REPEATINGS_RESCHEDULING = 2;
    // отодвинуть напоминание про все новые
    private static final int FLAG_NEW_REPEATINGS_SHIFT = 3;
    // перейти к напоминаниям
    private static final int FLAG_NEW_REPEATINGS_SHOW = 4;

    // запланировать отложенную проверку наличия незавершенных задач
    // а при их отсутствии - отменить все таймеры
    private static final int FLAG_SCHIFT_UNCOMPLETED_CHECKING = 5;
    // проверить наличие незавершенной и нотифицировать
    private static final int FLAG_CHECK_UNCOMPLETED_TASKS = 6;
    private static final int FLAG_SHOW_UNCOMPLETED_TASKS = 7;

    // TODO пока делаю копипаст LEARNING's И REPEATINGS's - все одинаково, только типы и сообщения разные, а также типы нотификаций
    //

    // типы нотификаций:
    //   "У вас есть новые повторения уроков"
    //   "У вас есть неоконченные повторения уроков"
    //   ("У вас есть недоформированные уроки") (логика под вопросом)
    //   "У вас есть уроки, ожидающие старта"
    //   "У вас есть незаконченные уроки"


    // все возможные события
    // 1. загрузка
    //      перепланировать напоминалку текущих
    //      перепланировать напоминалку запланированных повторений
    //      перепланировать напоминалку неоконченных собираться паков
    // 2. игрок смахнул нотификацию или закрыл приложение, после реакции
    //      если new_REPEATING  - перепланировать 10-20-30 мин позже
    //      если unfinished     - перепланировать на 10-20-30 мин позже
    // 3. юзер нажал, напомнить позже (сместить по времени и перепланировать позже)
    //       сдвинуть время и пересчитать
    // 4. начал повторять и закрыл приложение
    //       через 10-20-30 мин напомнить UNFINISHED

    @Inject
    NewRepeatsApi mNewRepeatsApi;
    @Inject
    LearnsApi mLearnsApi;
    @Inject
    UncompletedTaskApi mUncompletedTaskApi;


    public NotificationsPlannerService() {
        super("NotificationsPlannerService");
        NotificationsHelper.initChannels(this);
        MyLog.add("NotificationsPlannerService - default constructor");
    }

    public NotificationsPlannerService(String name) {
        super(name);
        NotificationsHelper.initChannels(this);
        MyLog.add("NotificationsPlannerService: "+name);
    }

    public static Intent getOnBootReSchedulingIntent(Context callerContext){
        Intent intent = new Intent(callerContext, NotificationsPlannerService.class);
        intent.putExtra(EXTRA_FLAG, FLAG_ON_BOOT_RESCHEDULING);
        return intent;
    }

    public static Intent getLearnCoursesReSchedulingIntent(Context callerContext, @Nullable LearnCourseMode targetLCMode){
        Intent intent = new Intent(callerContext, NotificationsPlannerService.class);
        intent.putExtra(EXTRA_FLAG, FLAG_NEW_REPEATINGS_RESCHEDULING);
        intent.putExtra(EXTRA_TARGET_MODE, targetLCMode);
        return intent;
    }


    public static Intent getLearnCoursesShiftIntent(Context callerContext, LearnCourseMode targetLCMode){
        Intent intent = new Intent(callerContext, NotificationsPlannerService.class);
        intent.putExtra(EXTRA_FLAG, FLAG_NEW_REPEATINGS_SHIFT);
        intent.putExtra(EXTRA_TARGET_MODE, targetLCMode);
        return intent;
    }

    public static Intent getLearnCoursesShowIntent(Context callerContext, LearnCourseMode targetLCMode){
        Intent intent = new Intent(callerContext, NotificationsPlannerService.class);
        intent.putExtra(EXTRA_FLAG, FLAG_NEW_REPEATINGS_SHOW);
        intent.putExtra(EXTRA_TARGET_MODE, targetLCMode);
        return intent;
    }

    public static Intent getSchiftUncompletedCheckingIntent(Context callerContext){
        Intent intent = new Intent(callerContext, NotificationsPlannerService.class);
        intent.putExtra(EXTRA_FLAG, FLAG_SCHIFT_UNCOMPLETED_CHECKING);
        return intent;
    }

    public static Intent getCheckUncompletedTasksIntent(Context callerContext){
        Intent intent = new Intent(callerContext, NotificationsPlannerService.class);
        intent.putExtra(EXTRA_FLAG, FLAG_CHECK_UNCOMPLETED_TASKS);
        return intent;
    }

    public static Intent getShowUncompletedTasksIntent(Context callerContext){
        Intent intent = new Intent(callerContext, NotificationsPlannerService.class);
        intent.putExtra(EXTRA_FLAG, FLAG_SHOW_UNCOMPLETED_TASKS);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int flag = intent.getIntExtra(EXTRA_FLAG, 0);
        LearnCourseMode targetMode = (LearnCourseMode)intent.getSerializableExtra(EXTRA_TARGET_MODE);

        MyLog.add("NotificationsPlannerService::onHandleIntent");

        switch (flag) {

            case FLAG_ON_BOOT_RESCHEDULING:
                getLearnsApi().rescheduleNewLearnCourses();
                getNewRepeatsApi().rescheduleNewRepeatings();
                getUncompletedTaskApi().checkAndNotifyAboutUncompletedTasks(true);
                return;

            case FLAG_NEW_REPEATINGS_RESCHEDULING:

                MyLog.add("FLAG_NEW_REPEATINGS_RESCHEDULING");

                // обновить нотификации и таймеры напоминания
                if (targetMode == LearnCourseMode.LEARN_WAITING) {
                    MyLog.add("LEARN_WAITING");
                    getLearnsApi().rescheduleNewLearnCourses();
                }
                if(targetMode == LearnCourseMode.REPEAT_WAITING) {
                    MyLog.add("REPEAT_WAITING");
                    getNewRepeatsApi().rescheduleNewRepeatings();
                }
                return;

            case FLAG_NEW_REPEATINGS_SHIFT:

                if(targetMode == LearnCourseMode.LEARN_WAITING) {
                    getLearnsApi().shiftLearnCourses();
                }
                if(targetMode == LearnCourseMode.REPEAT_WAITING) {
                    getNewRepeatsApi().shiftNewRepeatings();
                }
                return;

            case FLAG_NEW_REPEATINGS_SHOW:
                if(targetMode == LearnCourseMode.LEARN_WAITING) {
                    showNewLearnings();
                } else
                if(targetMode == LearnCourseMode.REPEAT_WAITING) {
                    showNewRepeatings();
                }
                return;

            case FLAG_SCHIFT_UNCOMPLETED_CHECKING:
                getUncompletedTaskApi().shiftUncompletedChecking();
                return;
            case FLAG_CHECK_UNCOMPLETED_TASKS:
                getUncompletedTaskApi().checkAndNotifyAboutUncompletedTasks(false);
                return;
            case FLAG_SHOW_UNCOMPLETED_TASKS:
                showUncompletedTasks();
                return;
        }
    }

    private void showNewLearnings(){
        showNewCourses(getLearnsApi().getNewLearnCourses());
    }

    private void showNewRepeatings() {
        showNewCourses(getNewRepeatsApi().getNewLearnCourses());
    }

    private void showNewCourses(List<LearnCourseEntity> courses) {
        Long[] courseIds = LearnCourseHelper.getLearnCourseIds(courses);


        if (courseIds.length == 0) {
            // TODO error
            return;
        } else if (courseIds.length == 1) {
            Intent activityIntent = CourseInfoActivity.newActivityIntent(this, courseIds[0]);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(activityIntent);
            return;
        } else{
            Intent activityIntent = CoursesListActivity.newActivityForCourseIdsIntent(this, courseIds);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(activityIntent);
        }
    }

    private void showUncompletedTasks() {
        List<LearnCourseEntity> uncompletedCourses = mUncompletedTaskApi.getUncompletedCourses();
        if(uncompletedCourses.size() == 0){
            // TODO error
            return;
        } else
        if(uncompletedCourses.size() == 1){
            Intent activityIntent = CourseInfoActivity.newActivityIntent(this, uncompletedCourses.get(0).getId());
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(activityIntent);
            return;
        } else {
            Intent activityIntent = CoursesListActivity.newActivityForModesIntent(this, mUncompletedTaskApi.getUncompletedCourseModes());
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(activityIntent);
        }
    }

    @Override
    public void onCreate() {

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate();
    }

    @Override
    public void onDestroy() {

        /*
        if(mNewRepeatsInteractor != null){
            mNewRepeatsInteractor.dispose();
            mNewRepeatsInteractor = null;
        }

        if(mLearnsInteractor != null){
            mLearnsInteractor.dispose();
            mLearnsInteractor = null;
        }*/

        /*
        if(mUncompletedTaskApi != null){
            mUncompletedTaskApi.dispose();
            mUncompletedTaskApi = null;
        }*/

        super.onDestroy();
    }

    private NewRepeatsApi getNewRepeatsApi() {
        /*
        if(mNewRepeatsInteractor == null){
            mNewRepeatsInteractor = new NewRepeatsApi(this);
        }*/
        return mNewRepeatsApi;
    }

    private LearnsApi getLearnsApi() {
        /*
        if(mLearnsInteractor == null){

            mLearnsInteractor = new LearnsApi(this);
        }*/
        return mLearnsApi;
    }

    private UncompletedTaskApi getUncompletedTaskApi() {
        /*
        if(mUncompletedTaskApi == null){
            mUncompletedTaskApi = new UncompletedTaskApi();
        }*/
        return mUncompletedTaskApi;
    }

}
