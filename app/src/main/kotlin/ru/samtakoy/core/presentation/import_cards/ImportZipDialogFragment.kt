package ru.samtakoy.core.presentation.import_cards

import android.net.Uri
import android.os.Bundle
import kotlinx.coroutines.rx2.await
import ru.samtakoy.R
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogFragment
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter.IProgressWorker
import ru.samtakoy.features.import_export.ImportApi
import ru.samtakoy.features.import_export.utils.ImportCardsOpts
import javax.inject.Inject

class ImportZipDialogFragment : ProgressDialogFragment() {

    @Inject
    internal lateinit var mImportApi: ImportApi

    @Inject
    internal lateinit var mResources: Resources

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    // --
    override fun createWorkerImpl(): IProgressWorker {
        val args = requireArguments()
        val selectedFileUri = args.getParcelable<Uri>(ARG_FILE_URI)!!
        val opts = (args.getSerializable(ARG_OPTS)) as ImportCardsOpts

        return object : IProgressWorker {
            override suspend fun doWork() {
                mImportApi.batchUpdateFromZip(selectedFileUri, opts).await()
            }

            override fun getTitle(): String {
                return mResources.getString(R.string.fragment_dialog_pack_import_title)
            }

            override fun getErrorText(): String {
                return mResources.getString(R.string.fragment_dialog_pack_import_error_msg)
            }
        }
    } // --

    companion object {
        const val TAG: String = "ImportZipDialogFragment"

        private const val ARG_FILE_URI = "ARG_FILE_URI"
        private const val ARG_OPTS = "ARG_OPTS"

        fun newFragment(
            selectedFileUri: Uri,
            opts: ImportCardsOpts
        ): ImportZipDialogFragment {
            val result = ImportZipDialogFragment()
            val args = Bundle()
            args.putParcelable(ARG_FILE_URI, selectedFileUri)
            args.putSerializable(ARG_OPTS, opts)
            result.setArguments(args)
            return result
        }
    }
}
