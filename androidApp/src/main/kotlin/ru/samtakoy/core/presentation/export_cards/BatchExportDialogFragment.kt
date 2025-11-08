package ru.samtakoy.core.presentation.export_cards

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.R
import ru.samtakoy.core.presentation.export_cards.mv.BatchExportViewModel
import ru.samtakoy.core.presentation.export_cards.mv.BatchExportViewModelImpl
import ru.samtakoy.presentation.base.observe
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner

/**
 * TODO это все устарело и будет переписано (удалено)
 * */
class BatchExportDialogFragment : AppCompatDialogFragment(), ViewModelOwner {

    private val viewModel: BatchExportViewModel by viewModel<BatchExportViewModelImpl> {
        parametersOf(
            requireArguments().getParcelable<BatchExportType>(ARG_EXPORT_TYPE) as BatchExportType
        )
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_batch_export, null)
        return AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
    }

    private fun onAction(action: BatchExportViewModel.Action) {
        when (action) {
            BatchExportViewModel.Action.ExitOk -> exitOk()
            is BatchExportViewModel.Action.ExitWithError -> exitWithError(action.errorText)
        }
    }

    private fun exitWithError(errorText: String) {
        Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show()
        getTargetFragment()!!.onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null)
        dismiss()
    }

    private fun exitOk() {
        if (getTargetFragment() == null) {
            return
        }
        getTargetFragment()!!.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null)
        dismiss()
    }

    companion object {
        const val TAG: String = "BatchExportDialogFragment"

        private const val ARG_EXPORT_TYPE = "ARG_EXPORT_TYPE"

        fun newQPacksFragment(
            type: BatchExportType,
            targetFragment: Fragment,
            targetReqCode: Int
        ): BatchExportDialogFragment {
            val result = BatchExportDialogFragment()
            result.setArguments(
                bundleOf(ARG_EXPORT_TYPE to type)
            )
            result.setTargetFragment(targetFragment, targetReqCode)
            return result
        }

        fun newCoursesFragment(
            targetFragment: Fragment,
            targetReqCode: Int
        ): BatchExportDialogFragment {
            val result = BatchExportDialogFragment()
            result.setArguments(
                bundleOf(ARG_EXPORT_TYPE to BatchExportType.Courses)
            )
            result.setTargetFragment(targetFragment, targetReqCode)
            return result
        }
    }
}
