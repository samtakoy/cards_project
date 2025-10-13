package ru.samtakoy.core.presentation.settings

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ru.samtakoy.R
import ru.samtakoy.common.utils.MyLog.add
import ru.samtakoy.core.presentation.showDialogFragment

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        findPreference<Preference>("pref_key_reset_db")?.setOnPreferenceClickListener(
            Preference.OnPreferenceClickListener { preference: Preference? ->
                showClearDbConfirmDialog()
                true
            }
        )
    }

    private fun showClearDbConfirmDialog() {
        add("-- showClearDbConfirmDialog")

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.confirm_dialog_title)
            .setCancelable(true)
            .setMessage(R.string.clear_db_confirmation_msg)
            .setPositiveButton(
                R.string.btn_ok,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->
                    showClearDbDialog()
                    dialogInterface!!.dismiss()
                })
            .show()
    }

    private fun showClearDbDialog() {
        add("-- showClearDbDialog")

        val dialog: DialogFragment =
            ClearDbDialogFragment.newFragment()
        showDialogFragment(dialog, this, ClearDbDialogFragment.TAG)
    }

    companion object {
        fun newFragment(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
