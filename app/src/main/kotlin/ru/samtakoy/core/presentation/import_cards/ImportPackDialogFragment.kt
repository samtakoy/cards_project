package ru.samtakoy.core.presentation.import_cards

import android.net.Uri
import android.os.Bundle
import kotlinx.coroutines.rx2.await
import org.koin.android.ext.android.inject
import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogFragment
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter.IProgressWorker
import ru.samtakoy.features.import_export.ImportApi
import ru.samtakoy.features.import_export.utils.ImportCardsOpts

class ImportPackDialogFragment : ProgressDialogFragment() {
    private val mImportApi: ImportApi by inject()
    private val mResources: Resources by inject()

    override fun createWorkerImpl(): IProgressWorker {
        val args = requireArguments()
        val selectedFileUri = args.getParcelable<Uri>(ARG_FILE_URI)!!
        val targetThemeId = args.getLong(ARG_THEME_ID)
        val opts = (args.getSerializable(ARG_OPTS)) as ImportCardsOpts

        return object : IProgressWorker {
            override suspend fun doWork() {
                mImportApi.loadCardsFromFile(selectedFileUri, targetThemeId, opts).await()
            }

            override fun getTitle(): String {
                return mResources.getString(R.string.fragment_dialog_pack_import_title)
            }

            override fun getErrorText(): String {
                return mResources.getString(R.string.fragment_dialog_pack_import_error_msg)
            }
        }
    }

    companion object {
        const val TAG: String = "ImportPackDialogFragment"

        private const val ARG_FILE_URI = "ARG_FILE_URI"
        private const val ARG_THEME_ID = "ARG_THEME_ID"
        private const val ARG_OPTS = "ARG_OPTS"

        fun newFragment(
            selectedFileUri: Uri,
            targetThemeId: Long,
            opts: ImportCardsOpts
        ): ImportPackDialogFragment {
            val result = ImportPackDialogFragment()
            val args = Bundle()
            args.putLong(ARG_THEME_ID, targetThemeId)
            args.putParcelable(ARG_FILE_URI, selectedFileUri)
            args.putSerializable(ARG_OPTS, opts)
            result.setArguments(args)
            return result
        }
    }
}
