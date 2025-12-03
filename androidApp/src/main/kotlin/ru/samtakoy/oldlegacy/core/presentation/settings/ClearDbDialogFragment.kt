package ru.samtakoy.oldlegacy.core.presentation.settings

import org.koin.android.ext.android.inject
import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.oldlegacy.core.presentation.MainActivity
import ru.samtakoy.oldlegacy.core.presentation.RouterHolder
import ru.samtakoy.oldlegacy.core.presentation.progress_dialog.ProgressDialogFragment
import ru.samtakoy.oldlegacy.core.presentation.progress_dialog.ProgressDialogPresenter

class ClearDbDialogFragment : ProgressDialogFragment() {
    private val cardsInteractor: CardInteractor by inject()
    private val myResources: Resources by inject()

    override fun createWorkerImpl(): ProgressDialogPresenter.IProgressWorker {
        return object : ProgressDialogPresenter.IProgressWorker {
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
            startActivity(MainActivity.Companion.newRootActivity(getContext()))
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
