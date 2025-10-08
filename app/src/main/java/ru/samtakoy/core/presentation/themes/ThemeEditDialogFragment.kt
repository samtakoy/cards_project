package ru.samtakoy.core.presentation.themes

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.samtakoy.R

class ThemeEditDialogFragment : DialogFragment() {
    private var mInputText: EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = getArguments()
        val defaultThemeName = args!!.getString(ARG_TEXT)

        val v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_theme_add, null)
        mInputText = v.findViewById<View?>(R.id.fragment_dialog_theme_add_input) as EditText
        mInputText!!.setText(defaultThemeName)

        return AlertDialog.Builder(requireActivity())
            .setView(v)
            .setCancelable(true)
            .setPositiveButton(
                android.R.string.ok,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int -> trySendResult() }
            )
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDialog()!!.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    private fun trySendResult() {
        val result = Bundle()
        result.putString(RESULT_EXTRA_TEXT, mInputText!!.getText().toString().trim { it <= ' ' })
        getParentFragmentManager().setFragmentResult(REQ_KEY, result)
    }

    companion object {
        const val TAG: String = "ThemeEditDialogFragment"
        const val RESULT_EXTRA_TEXT: String = "RESULT_EXTRA_TEXT"
        private const val ARG_TEXT = "ARG_TEXT"

        var REQ_KEY: String = "REQ_CODE_INPUT_THEME_TITLE"

        fun newDialog(defaultThemeName: String?): ThemeEditDialogFragment {
            val result = ThemeEditDialogFragment()
            val args = Bundle()
            args.putString(ARG_TEXT, defaultThemeName)
            result.setArguments(args)
            return result
        }
    }
}
