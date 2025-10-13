package ru.samtakoy.features.notifications

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.navigation.NavDeepLinkBuilder
import ru.samtakoy.R
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.presentation.courses.info.CourseInfoFragment.Companion.buildBundle
import ru.samtakoy.core.presentation.courses.list.CoursesListFragment
import ru.samtakoy.common.utils.MyLog.add
import ru.samtakoy.data.learncourse.utils.LearnCourseHelper
import ru.samtakoy.domain.learncourse.LearnCourse
import ru.samtakoy.domain.learncourse.LearnCourseMode
import ru.samtakoy.features.notifications.learn_courses.LearnsApi
import ru.samtakoy.features.notifications.learn_courses.NewRepeatsApi
import ru.samtakoy.features.notifications.learn_courses.UncompletedTaskApi
import javax.inject.Inject

class NotificationsPlannerService : IntentService {
    /*
          if(mNewRepeatsInteractor == null){
              mNewRepeatsInteractor = new NewRepeatsApi(this);
          }*/
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
    internal lateinit var newRepeatsApi: NewRepeatsApi

    @Inject
    internal lateinit var learnsApi: LearnsApi

    @Inject
    internal lateinit var mUncompletedTaskApi: UncompletedTaskApi

    constructor() : super("NotificationsPlannerService") {
        NotificationsHelper.initChannels(this)
        add("NotificationsPlannerService - default constructor")
    }

    constructor(name: String?) : super(name) {
        NotificationsHelper.initChannels(this)
        add("NotificationsPlannerService: " + name)
    }

    override fun onHandleIntent(intent: Intent?) {
        intent ?: return
        val flag = intent.getIntExtra(EXTRA_FLAG, 0)
        val targetMode = intent.getSerializableExtra(EXTRA_TARGET_MODE) as LearnCourseMode?

        add("NotificationsPlannerService::onHandleIntent")

        when (flag) {
            FLAG_ON_BOOT_RESCHEDULING -> {
                this.learnsApi.rescheduleNewLearnCourses()
                this.newRepeatsApi.rescheduleNewRepeatings()
                mUncompletedTaskApi.checkAndNotifyAboutUncompletedTasks(true)
                return
            }
            FLAG_NEW_REPEATINGS_RESCHEDULING -> {
                add("FLAG_NEW_REPEATINGS_RESCHEDULING")

                // обновить нотификации и таймеры напоминания
                if (targetMode == LearnCourseMode.LEARN_WAITING) {
                    add("LEARN_WAITING")
                    this.learnsApi.rescheduleNewLearnCourses()
                }
                if (targetMode == LearnCourseMode.REPEAT_WAITING) {
                    add("REPEAT_WAITING")
                    this.newRepeatsApi.rescheduleNewRepeatings()
                }
                return
            }
            FLAG_NEW_REPEATINGS_SHIFT -> {
                if (targetMode == LearnCourseMode.LEARN_WAITING) {
                    this.learnsApi.shiftLearnCourses()
                }
                if (targetMode == LearnCourseMode.REPEAT_WAITING) {
                    this.newRepeatsApi.shiftNewRepeatings()
                }
                return
            }
            FLAG_NEW_REPEATINGS_SHOW -> {
                if (targetMode == LearnCourseMode.LEARN_WAITING) {
                    showNewLearnings()
                } else if (targetMode == LearnCourseMode.REPEAT_WAITING) {
                    showNewRepeatings()
                }
                return
            }
            FLAG_SHIFT_UNCOMPLETED_CHECKING, FLAG_PLAN_UNCOMPLETED_CHECKING -> {
                // пока действуем одинаково
                mUncompletedTaskApi.shiftUncompletedChecking()
                return
            }
            FLAG_CHECK_UNCOMPLETED_TASKS_AND_NOTIFY_NOW -> {
                mUncompletedTaskApi.checkAndNotifyAboutUncompletedTasks(false)
                return
            }
            FLAG_SHOW_UNCOMPLETED_TASKS -> {
                showUncompletedTasks()
                return
            }
        }
    }

    private fun showNewLearnings() {
        showNewCourses(this.learnsApi.newLearnCourses)
    }

    private fun showNewRepeatings() {
        showNewCourses(this.newRepeatsApi.newLearnCourses)
    }

    private fun showNewCourses(courses: List<LearnCourse>) {
        val courseIds = LearnCourseHelper.getLearnCourseIds(courses)


        if (courseIds.size == 0) {
            // TODO error
            return
        } else if (courseIds.size == 1) {
            startCourseInfoActivity(courseIds[0]!!)
            return
        } else {
            showCoursesByIds(courseIds)
        }
    }

    private fun startCourseInfoActivity(courseId: Long) {
        /*
               Intent activityIntent = CourseInfoActivity.newActivityIntent(this, courseId);
               activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(activityIntent);
               */

        //

        val pIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_graph)
            .setDestination(R.id.courseInfoFragment)
            .setArguments(buildBundle(courseId))
            .createPendingIntent()

        try {
            pIntent.send()
        } catch (e: PendingIntent.CanceledException) {
            add(TAG, e)
        }
    }

    private fun showUncompletedTasks() {
        val uncompletedCourses: List<LearnCourse?> = mUncompletedTaskApi.uncompletedCourses
        if (uncompletedCourses.size == 0) {
            // nothing to show - open empty courses list?
            showCoursesByIds(arrayOf<Long>())
            return
        } else if (uncompletedCourses.size == 1) {
            startCourseInfoActivity(uncompletedCourses.get(0)!!.id)
            return
        } else {
            showCoursesByModes(mUncompletedTaskApi.uncompletedCourseModes)
        }
    }

    override fun onCreate() {
        Di.appComponent.inject(this)

        super.onCreate()
    }

    override fun onDestroy() {
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

        super.onDestroy()
    }

    private fun showCoursesByIds(courseIds: Array<Long>?) {
        /*
        Intent activityIntent = CoursesListActivity.newActivityForCourseIdsIntent(this, courseIds);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);*/

        showCoursesActivityWithParams(null, courseIds)
    }

    private fun showCoursesByModes(modes: List<LearnCourseMode>) {
        /*Intent activityIntent = CoursesListActivity.newActivityForModesIntent(this, modes);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);*/

        showCoursesActivityWithParams(modes, null)
    }

    private fun showCoursesActivityWithParams(modes: List<LearnCourseMode>?, courseIds: Array<Long>?) {
        val pIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_graph)
            .setDestination(R.id.coursesListFragment)
            .setArguments(
                CoursesListFragment.buildBundle(0L, modes, courseIds)
            )
            .createPendingIntent()

        try {
            pIntent.send()
        } catch (e: PendingIntent.CanceledException) {
            add(TAG, e)
        }
    }

    companion object {
        private const val TAG = "NotificationsPlannerSer"

        // типизирование интента
        private const val EXTRA_FLAG = "EXTRA_FLAG"
        private const val EXTRA_TARGET_MODE = "EXTRA_TARGET_MODE"

        //private static final String EXTRA_ON_BOOT = "EXTRA_ON_BOOT";
        // перепланировать все
        private const val FLAG_ON_BOOT_RESCHEDULING = 1

        private const val FLAG_NEW_REPEATINGS_RESCHEDULING = 2

        // отодвинуть напоминание про все новые
        private const val FLAG_NEW_REPEATINGS_SHIFT = 3

        // перейти к напоминаниям
        private const val FLAG_NEW_REPEATINGS_SHOW = 4

        // запланировать отложенную проверку наличия незавершенных задач
        // а при их отсутствии - отменить все таймеры
        // при смахивании нотификации
        private const val FLAG_SHIFT_UNCOMPLETED_CHECKING = 5

        // приложение запрашивает проверить незавершенные задачи позднее
        private const val FLAG_PLAN_UNCOMPLETED_CHECKING = 6

        // проверить наличие незавершенной и нотифицировать
        private const val FLAG_CHECK_UNCOMPLETED_TASKS_AND_NOTIFY_NOW = 7
        private const val FLAG_SHOW_UNCOMPLETED_TASKS = 8

        fun getOnBootReSchedulingIntent(callerContext: Context?): Intent {
            val intent = Intent(callerContext, NotificationsPlannerService::class.java)
            intent.putExtra(EXTRA_FLAG, FLAG_ON_BOOT_RESCHEDULING)
            return intent
        }

        fun getLearnCoursesReSchedulingIntent(callerContext: Context?, targetLCMode: LearnCourseMode?): Intent {
            val intent = Intent(callerContext, NotificationsPlannerService::class.java)
            intent.putExtra(EXTRA_FLAG, FLAG_NEW_REPEATINGS_RESCHEDULING)
            intent.putExtra(EXTRA_TARGET_MODE, targetLCMode)
            return intent
        }

        fun getLearnCoursesShiftIntent(callerContext: Context?, targetLCMode: LearnCourseMode?): Intent {
            val intent = Intent(callerContext, NotificationsPlannerService::class.java)
            intent.putExtra(EXTRA_FLAG, FLAG_NEW_REPEATINGS_SHIFT)
            intent.putExtra(EXTRA_TARGET_MODE, targetLCMode)
            return intent
        }

        fun getLearnCoursesShowIntent(callerContext: Context?, targetLCMode: LearnCourseMode?): Intent {
            val intent = Intent(callerContext, NotificationsPlannerService::class.java)
            intent.putExtra(EXTRA_FLAG, FLAG_NEW_REPEATINGS_SHOW)
            intent.putExtra(EXTRA_TARGET_MODE, targetLCMode)
            return intent
        }

        fun getSchiftUncompletedCheckingIntent(callerContext: Context?): Intent {
            val intent = Intent(callerContext, NotificationsPlannerService::class.java)
            intent.putExtra(EXTRA_FLAG, FLAG_SHIFT_UNCOMPLETED_CHECKING)
            return intent
        }

        fun getPlanUncompletedCheckingIntent(callerContext: Context?): Intent {
            val intent = Intent(callerContext, NotificationsPlannerService::class.java)
            intent.putExtra(EXTRA_FLAG, FLAG_PLAN_UNCOMPLETED_CHECKING)
            return intent
        }

        fun getCheckUncompletedTasksIntent(callerContext: Context?): Intent {
            val intent = Intent(callerContext, NotificationsPlannerService::class.java)
            intent.putExtra(EXTRA_FLAG, FLAG_CHECK_UNCOMPLETED_TASKS_AND_NOTIFY_NOW)
            return intent
        }

        fun getShowUncompletedTasksIntent(callerContext: Context?): Intent {
            val intent = Intent(callerContext, NotificationsPlannerService::class.java)
            intent.putExtra(EXTRA_FLAG, FLAG_SHOW_UNCOMPLETED_TASKS)
            return intent
        }
    }
}
