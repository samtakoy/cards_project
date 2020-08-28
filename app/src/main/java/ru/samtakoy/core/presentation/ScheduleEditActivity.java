package ru.samtakoy.core.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.samtakoy.core.database.room.entities.elements.Schedule;


// TODO remove
public class ScheduleEditActivity extends SingleFragmentActivity{

    private static final String ARG_SCHEDULE_STRING = "ARG_SCHEDULE_STRING";

    public static final String RESULT_SCHEDULE_STRING = "RESULT_SCHEDULE_STRING";

    public static Intent newActivityIntent(Context ctx, Schedule schedule){
        Intent result = new Intent(ctx, ScheduleEditActivity.class);
        result.putExtra(ARG_SCHEDULE_STRING, schedule == null ? null : schedule.toString());
        return result;
    }

    @Override
    protected Fragment createFragment() {
        return ScheduleEditFragment.newFragment(mSchedule);
    }

    private Schedule mSchedule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSchedule = new Schedule();
        String strSchedule = getIntent().getStringExtra(ARG_SCHEDULE_STRING);
        if(strSchedule != null){
            mSchedule.initFromString(strSchedule);
        }
    }

    /*
    @Override
    public void onScheduleSelect(Schedule schedule) {
        Intent resultData = new Intent();
        resultData.putExtra(RESULT_SCHEDULE_STRING, schedule.toString());
        setResult(Activity.RESULT_OK, resultData);
    }/***/

}
