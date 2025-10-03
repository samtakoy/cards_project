package ru.samtakoy.core.presentation.import_cards

import android.os.Bundle
import kotlinx.coroutines.rx2.await
import ru.samtakoy.R
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogFragment
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter.IProgressWorker
import ru.samtakoy.features.import_export.ImportApi
import ru.samtakoy.features.import_export.utils.ImportCardsOpts
import javax.inject.Inject

class BatchImportDialogFragment : ProgressDialogFragment() {

    @Inject
    internal lateinit var mImportApi: ImportApi

    @Inject
    internal lateinit var mResources: Resources

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun createWorkerImpl(): IProgressWorker {
        val args = requireArguments()
        val dirPath = args.getString(ARG_DIR_PATH)!!
        val targetThemeId = args.getLong(ARG_THEME_ID)
        val opts = (args.getSerializable(ARG_OPTS)) as ImportCardsOpts

        return object : IProgressWorker {
            override suspend fun doWork() {
                return mImportApi.batchLoadFromFolder(dirPath, targetThemeId, opts).await()
            }

            override fun getTitle(): String {
                return mResources.getString(R.string.fragment_dialog_batch_import_title)
            }

            override fun getErrorText(): String {
                return mResources.getString(R.string.fragment_dialog_batch_import_error_msg)
            }
        }
    }

    companion object {
        const val TAG: String = "BatchImportDialogFragment"
        private const val ARG_DIR_PATH = "ARG_DIR_PATH"
        private const val ARG_OPTS = "ARG_OPTS"
        private const val ARG_THEME_ID = "ARG_THEME_ID"

        fun newFragment(
            dirPath: String,
            targetThemeId: Long,
            opts: ImportCardsOpts
        ): BatchImportDialogFragment {
            val result = BatchImportDialogFragment()
            val args = Bundle()
            args.putString(ARG_DIR_PATH, dirPath)
            args.putLong(ARG_THEME_ID, targetThemeId)
            args.putSerializable(ARG_OPTS, opts)
            result.setArguments(args)
            return result
        }
    }
}
