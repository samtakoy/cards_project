package ru.samtakoy.core.presentation.misc.edit_text_block

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import ru.samtakoy.R

class EditTextBlockDialogFragment : DialogFragment() {
    var mEditText: EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val text = requireArguments().getString(ARG_TEXT)
        val v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_edit_text_block, null)

        mEditText = v.findViewById<EditText>(R.id.edit_text)
        mEditText!!.setText(text)

        val builder = AlertDialog.Builder(requireContext())
            .setView(v)
            .setCancelable(true)
            .setOnCancelListener(DialogInterface.OnCancelListener { dialogInterface: DialogInterface? -> onCancel() })
            .setPositiveButton(
                R.string.btn_save,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int -> onOk() })
            .setNegativeButton(
                R.string.btn_cancel,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int -> onCancel() })
        return builder.create()
    }

    private fun onOk() {
        val resultText = mEditText!!.getText().toString()

        val result = Intent()
        result.putExtra(RESULT_TEXT, resultText)


        getTargetFragment()!!.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result)
        dismiss()
    }

    private fun onCancel() {
        getTargetFragment()!!.onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null)
        dismiss()
    }

    companion object {
        const val TAG: String = "EditTextBlockDialogFrag"

        private const val ARG_TEXT = "ARG_TEXT"
        const val RESULT_TEXT: String = "RESULT_TEXT"

        fun newInstance(
            text: String,
            targetFragment: Fragment,
            targetFragmentRequestCode: Int
        ): EditTextBlockDialogFragment {
            val args = Bundle()
            args.putString(ARG_TEXT, text)

            val fragment = EditTextBlockDialogFragment()
            fragment.setArguments(args)
            fragment.setTargetFragment(targetFragment, targetFragmentRequestCode)
            return fragment
        }
    }
}
