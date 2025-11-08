package ru.samtakoy.core.presentation.progress_dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.R
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter.IProgressWorker
import ru.samtakoy.core.presentation.progress_dialog.vm.ProgressDialogViewModel
import ru.samtakoy.core.presentation.progress_dialog.vm.ProgressDialogViewModelImpl
import ru.samtakoy.presentation.base.observe
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner

/**
 * TODO это все устарело и будет переписано (удалено)
 * */
abstract class ProgressDialogFragment : AppCompatDialogFragment(), ViewModelOwner {
    protected abstract fun createWorkerImpl(): IProgressWorker

    private var titleTextView: TextView? = null

    private val viewModel: ProgressDialogViewModel by viewModel<ProgressDialogViewModelImpl> {
        parametersOf(
            // worker
            createWorkerImpl()
        )
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_dialog_progress, null)
        initView(dialogView!!)
        return AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    private fun onAction(action: ProgressDialogViewModel.Action) {
        when (action) {
            ProgressDialogViewModel.Action.ExitCanceled -> exitCanceled()
            ProgressDialogViewModel.Action.ExitOk -> exitOk()
            is ProgressDialogViewModel.Action.ShowErrorMessage -> showError(action.message)
        }
    }

    private fun onViewState(state: ProgressDialogViewModel.State) {
        titleTextView!!.setText(state.title)
    }

    protected fun initView(view: View) {
        titleTextView = view.findViewById(R.id.title)
    }

    private fun showError(message: String) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun exitCanceled() {
        getTargetFragment()?.onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null)
        dismiss()
    }

    private fun exitOk() {
        if (getTargetFragment() != null) {
            getTargetFragment()?.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null)
        }
        dismiss()
    }
}
