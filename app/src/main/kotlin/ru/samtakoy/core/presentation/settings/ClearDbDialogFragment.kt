package ru.samtakoy.core.presentation.settings

import android.os.Bundle
import ru.samtakoy.R
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.core.presentation.MainActivity
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogFragment
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter.IProgressWorker
import javax.inject.Inject

class ClearDbDialogFragment : ProgressDialogFragment() {
    @Inject
    internal lateinit var mCardsInteractor: CardInteractor

    @Inject
    internal lateinit var mResources: Resources

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun createWorkerImpl(): IProgressWorker {
        return object : IProgressWorker {
            override suspend fun doWork() {
                mCardsInteractor.clearDb()
            }

            override fun getErrorText(): String {
                return mResources!!.getString(R.string.clear_db_error)
            }

            override fun getTitle(): String {
                return mResources!!.getString(R.string.clear_db_title)
            }

            override fun onComplete() {
                navigateToBlankRootScreen()
            }
        }
    }

    private fun navigateToBlankRootScreen() {
        val rh = getActivity() as RouterHolder?
        if (rh != null) {
            startActivity(MainActivity.newRootActivity(getContext()))
        }
    }

    companion object {
        const val TAG: String = "ClearDbDialogFragment"

        @JvmStatic fun newFragment(): ClearDbDialogFragment {
            val result = ClearDbDialogFragment()
            return result
        }
    }
}
