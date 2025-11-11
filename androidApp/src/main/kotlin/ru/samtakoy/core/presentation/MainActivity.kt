package ru.samtakoy.core.presentation

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.navigation.NavigationView
import ru.samtakoy.R
import ru.samtakoy.core.oldutils.FragmentViewModelLifecycleCallbacks
import ru.samtakoy.common.utils.log.MyLog.add

class MainActivity : AppCompatActivity(), RouterHolder {
    private var mToolbar: Toolbar? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mNavigationView: NavigationView? = null
    private var mActionBarDrawerToggle: ActionBarDrawerToggle? = null

    private var mNavController: NavController? = null

    override fun onDestroy() {
        add(" %% DESTROY_ACTIVITY___ ")
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Подписка на данные вью моделей фрагментов
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(
            FragmentViewModelLifecycleCallbacks(),
            true
        )

        setContentView(R.layout.activity_main)


        mNavController = findNavController(this, R.id.nav_host_fragment_container)

        mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)

        mDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        mNavigationView = findViewById<NavigationView>(R.id.navigation_view)

        mActionBarDrawerToggle = ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.main_drawer_open_nav_drawer, R.string.main_drawer_close_nav_drawer
        )
        mDrawerLayout!!.addDrawerListener(mActionBarDrawerToggle!!)
        mActionBarDrawerToggle!!.syncState()

        setupActionBarWithNavController(this, mNavController!!, mDrawerLayout)
        setupWithNavController(mNavigationView!!, mNavController!!)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mActionBarDrawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mActionBarDrawerToggle!!.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                mNavController!!.popBackStack()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override val navController: NavController
        get() = mNavController!!

    override fun onSupportNavigateUp(): Boolean {
        return mNavController!!.navigateUp()
    }

    companion object {
        fun newRootActivity(context: Context?): Intent {
            val aIntent = Intent(context, MainActivity::class.java)
            aIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return aIntent
        }
    }
}
