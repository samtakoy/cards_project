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

import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.business.events.EventBusHolder;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.navigation.Screens;
import ru.samtakoy.core.screens.log.MyLog;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import ru.terrakok.cicerone.commands.Command;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RouterHolder, EventBusHolder {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    // --
    @Inject
    public Router mRouter;
    @Inject
    public EventBus mEventBus;
    @Inject
    NavigatorHolder mNavigatorHolder;
    private Navigator mNavigator = new SupportAppNavigator(this, R.id.container) {
        @Override
        public void applyCommands(Command[] commands) {
            super.applyCommands(commands);
            getSupportFragmentManager().executePendingTransactions();

        }
    };

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


MyLog.add(" %% CREATE_ACTIVITY___ " );

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

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

        /*
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(
        );/***/

        if(savedInstanceState == null) {
            setupInitialFragment();
        }
    }

    private void setupInitialFragment() {
        /*getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, ThemesListFragment.newFragment())
                .addToBackStack(ThemesListFragment.class.getSimpleName())
                .commit();*/
        mRouter.newRootScreen(new Screens.ThemeListScreen());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigatorHolder.setNavigator(mNavigator);
    }

    @Override
    protected void onPause() {
        mNavigatorHolder.removeNavigator();
        super.onPause();

    }

    @Override
    public Router getRouter() {
        return mRouter;
    }

    @Override
    public EventBus getEventBus() {
        return mEventBus;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        MyLog.add(":: "+item.getTitle().toString());

        switch (item.getItemId()){
            case R.id.nav_packs_themes:
                // по идее - это часть перехода навигации (TODO куда-то перенести, в AppNAvigator?)
                item.setChecked(true);

                getSupportActionBar().setTitle(item.getTitle());
                mRouter.newRootScreen(new Screens.ThemeListScreen());
                //mRouter.;
                break;
            case R.id.nav_packs_raw_list:
                // по идее - это часть перехода навигации (TODO куда-то перенести)
                item.setChecked(true);
                getSupportActionBar().setTitle(item.getTitle());
                mRouter.newRootScreen(new Screens.QPacksListScreen());
                break;
            case R.id.nav_courses:
                // по идее - это часть перехода навигации (TODO куда-то перенести)
                item.setChecked(true);
                getSupportActionBar().setTitle(item.getTitle());
                mRouter.newRootScreen(Screens.CoursesListScreen.allCoursesScreen());
                break;
            case R.id.nav_settings:
                getSupportActionBar().setTitle(item.getTitle());
                mRouter.navigateTo(new Screens.SettingsScreen());
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }



}
