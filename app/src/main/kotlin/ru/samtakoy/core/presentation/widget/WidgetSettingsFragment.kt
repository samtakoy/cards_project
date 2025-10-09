package ru.samtakoy.core.presentation.widget

import android.appwidget.AppWidgetManager
import androidx.core.os.bundleOf
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.presentation.qpack.list.QPacksListFragment

class WidgetSettingsFragment : QPacksListFragment() {

    private val widgetId by lazy {
        arguments?.getInt(KEY_WIDGET_ID) ?: AppWidgetManager.INVALID_APPWIDGET_ID
    }

    /* TODO переделать
    @Inject
    lateinit var mPresenterFactory2: WidgetSettingsPresenter.Factory
    override fun providePresenter(): QPacksListPresenter {
        return mPresenterFactory2.create(widgetId)
    }*/

    companion object {
        private const val KEY_WIDGET_ID = "KEY_WIDGET_ID"

        fun newInstance(widgetId: Int) = WidgetSettingsFragment().apply {
            arguments = bundleOf(
                KEY_WIDGET_ID to widgetId
            )
        }
    }

    override fun injectDependencies() {
        Di.appComponent.inject(this)
    }
}