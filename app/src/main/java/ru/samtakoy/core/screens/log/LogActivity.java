package ru.samtakoy.core.screens.log;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import ru.samtakoy.core.screens.SingleFragmentActivity;

public class LogActivity extends SingleFragmentActivity {


    public static Intent newActivityIntent(Context callerContext){
        return new Intent(callerContext, LogActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return LogFragment.newFragment();
    }
}
