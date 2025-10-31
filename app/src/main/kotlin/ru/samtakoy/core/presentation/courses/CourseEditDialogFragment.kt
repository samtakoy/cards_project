package ru.samtakoy.core.presentation.courses

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.samtakoy.R

class CourseEditDialogFragment : DialogFragment() {
    private var mInputText: EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = getArguments()
        val defaultInputText = args!!.getString(ARG_TEXT)

        /*
        val v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_theme_add, null)
        mInputText = v.findViewById<View?>(R.id.fragment_dialog_theme_add_input) as EditText
        mInputText!!.setText(defaultInputText)
         */
        val v = View(requireContext()) // TODO временная заглушка, класс будет удален

        //mInputText.requestFocus();
        return AlertDialog.Builder(requireActivity())
            .setView(v)
            .setCancelable(true)
            .setPositiveButton(
                android.R.string.ok,
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface?, i: Int) {
                        trySendResult()
                    }
                }
            )
            .create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getDialog()!!.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    private fun trySendResult() {
        if (getTargetFragment() == null) {
            return
        }
        val result = Intent()
        result.putExtra(RESULT_EXTRA_TEXT, mInputText!!.getText().toString().trim { it <= ' ' })
        getTargetFragment()!!.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result)
    }

    companion object {
        const val RESULT_EXTRA_TEXT: String = "RESULT_EXTRA_TEXT"
        private const val ARG_TEXT = "ARG_TEXT"

        fun newDialog(defaultInputText: String?): CourseEditDialogFragment {
            val result = CourseEditDialogFragment()
            val args = Bundle()
            args.putString(ARG_TEXT, defaultInputText)
            result.setArguments(args)
            return result
        }
    }
}
