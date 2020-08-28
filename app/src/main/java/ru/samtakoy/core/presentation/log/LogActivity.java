package ru.samtakoy.core.presentation.log;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import ru.samtakoy.core.presentation.SingleFragmentActivity;

public class LogActivity extends SingleFragmentActivity {


    public static Intent newActivityIntent(Context callerContext){
        return new Intent(callerContext, LogActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return LogFragment.newFragment();
    }
}
