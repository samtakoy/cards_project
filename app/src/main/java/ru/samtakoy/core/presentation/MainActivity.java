package ru.samtakoy.core.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ru.samtakoy.R;
import ru.samtakoy.core.app.di.Di;
import ru.samtakoy.core.presentation.base.viewmodel.FragmentViewModelLifecycleCallbacks;
import ru.samtakoy.core.presentation.log.MyLog;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RouterHolder {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private NavController mNavController;


    public static Intent newRootActivity(Context context) {
        Intent aIntent = new Intent(context, MainActivity.class);
        aIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return aIntent;
    }

    @Override
    protected void onDestroy() {

        MyLog.add(" %% DESTROY_ACTIVITY___ ");
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Di.appComponent.inject(this);

        super.onCreate(savedInstanceState);

        // Подписка на данные вью моделей фрагментов
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(
            new FragmentViewModelLifecycleCallbacks(),
            true
        );

        setContentView(R.layout.activity_main);


        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment_container);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.main_drawer_open_nav_drawer, R.string.main_drawer_close_nav_drawer
        );
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        NavigationUI.setupActionBarWithNavController(this, mNavController, mDrawerLayout);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);

        if (savedInstanceState == null) {
            setupInitialFragment();
        }

        Timber.tag("mytest").e("MainActivity created");
    }

    private void setupInitialFragment() {

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mNavController.popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public NavController getNavController() {
        return mNavController;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return mNavController.navigateUp();
    }
}
