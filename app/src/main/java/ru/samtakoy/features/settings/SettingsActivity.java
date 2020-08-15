package ru.samtakoy.features.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.screens.SingleFragmentActivity;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;

public class SettingsActivity extends SingleFragmentActivity implements RouterHolder {


    public static Intent newActivityIntent(Context context) {
        Intent result = new Intent(context, SettingsActivity.class);
        return result;
    }

    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newFragment();
    }

    @Inject Router mRouter;
    @Inject NavigatorHolder mNavigatorHolder;
    private Navigator mNavigator = new SupportAppNavigator(this, -1);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        mNavigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigatorHolder.setNavigator( mNavigator );
    }

    @Override
    public Router getRouter() {
        return mRouter;
    }
}
