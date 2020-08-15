package ru.samtakoy.core.screens.online_import;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import ru.samtakoy.core.screens.SingleFragmentActivity;

public class OnlineImportActivity extends SingleFragmentActivity {


    public static Intent newActivityIntent(Context context) {
        Intent intent = new Intent(context, OnlineImportActivity.class);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        return OnlineImportFragment.createInstance();
    }




}
