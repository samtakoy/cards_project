package ru.samtakoy.core.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.screens.log.MyLog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RouterHolder {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private NavController mNavController;


    public static Intent newRootActivity(Context context) {
        Intent aIntent = new Intent(context, MainActivity.class);
        aIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return aIntent;
    }
    // --


    @Override
    protected void onDestroy() {

MyLog.add(" %% DESTROY_ACTIVITY___ " );

        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment_container);


        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavigationView = findViewById(R.id.navigation_view);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.main_drawer_open_nav_drawer, R.string.main_drawer_close_nav_drawer
        );
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            setupInitialFragment();
        }

        //test();

    }


    private void setupInitialFragment() {

        //mRouter.newRootScreen(new Screens.ThemeListScreen());

    }

    @Override
    public NavController getNavController() {
        return mNavController;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_packs_themes:
                mNavController.popBackStack(R.id.themesListFragment, false);
                //mRouter.newRootScreen(new Screens.ThemeListScreen());
                //mRouter.;
                break;
            case R.id.nav_packs_raw_list:
                mNavController.navigate(R.id.qPacksListFragment);
                //mRouter.newRootScreen(new Screens.QPacksListScreen());
                break;
            case R.id.nav_courses:
                mNavController.navigate(R.id.coursesListFragment);
                //mRouter.newRootScreen(Screens.CoursesListScreen.allCoursesScreen());
                break;
            case R.id.nav_settings:
                changeTitle(item.getTitle());
                mNavController.navigate(R.id.settingsFragment);
                //mRouter.navigateTo(new Screens.SettingsScreen());
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void changeTitle(CharSequence title) {

        MyLog.add("new title: " + title);

        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {

        //if(!mNavController.navigateUp()){
        super.onBackPressed();
        //}

    }

    @Override
    public boolean onSupportNavigateUp() {
        return mNavController.navigateUp();
    }
}
