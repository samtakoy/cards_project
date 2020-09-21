package ru.samtakoy.core.data.local.database.room.entities.other;

import java.util.List;

import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity;


public class LearnCourseHelper {

    public static Long[] getLearnCourseIds(List<LearnCourseEntity> list) {
        Long[] result = new Long[list.size()];
        int i = 0;
        for (LearnCourseEntity learnCourse : list) {
            result[i++] = learnCourse.getId();
        }
        return result;
    }

}