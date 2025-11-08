package ru.samtakoy.core.presentation.settings

import org.koin.android.ext.android.inject
import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.core.presentation.MainActivity
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogFragment
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter.IProgressWorker

class ClearDbDialogFragment : ProgressDialogFragment() {
    private val cardsInteractor: CardInteractor by inject()
    private val myResources: Resources by inject()

    override fun createWorkerImpl(): IProgressWorker {
        return object : IProgressWorker {
            override suspend fun doWork() {
                cardsInteractor.clearDb()
            }

            override fun getErrorText(): String {
                return myResources!!.getString(R.string.clear_db_error)
            }

            override fun getTitle(): String {
                return myResources!!.getString(R.string.clear_db_title)
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
