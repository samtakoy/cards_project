package ru.samtakoy.core.presentation.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.SingleFragmentActivity
import ru.samtakoy.common.utils.log.MyLog

class WidgetSettingsActivity : SingleFragmentActivity(), RouterHolder {

    private val widgetId by lazy {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    override fun createFragment(): Fragment = WidgetSettingsFragment.newInstance(widgetId)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        MyLog.add("WidgetSettingsActivity::onCreate()")
        super.onCreate(savedInstanceState, persistentState)
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        setResult(RESULT_CANCELED, resultValue)
    }

    // override fun getNavController() = null
    override val navController: NavController
        get() = TODO("Not yet implemented")
}