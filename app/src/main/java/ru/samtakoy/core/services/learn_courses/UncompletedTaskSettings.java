package ru.samtakoy.core.services.learn_courses;

public class UncompletedTaskSettings {

    /** задержка при смахивании */
    private int mShiftMillis;
    /** на проверку при открытии курса */
    private int mOpenCourseShiftMillis;

    public  UncompletedTaskSettings(int shiftMillis, int openCourseShiftMillis){
        mShiftMillis = shiftMillis;
        mOpenCourseShiftMillis = openCourseShiftMillis;
    }

    public int getShiftMillis() {
        return mShiftMillis;
    }

    public int getOpenCourseShiftMillis() {
        return mOpenCourseShiftMillis;
    }
}
