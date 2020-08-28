package ru.samtakoy.features.settings;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceFragmentCompat;

import ru.samtakoy.R;
import ru.samtakoy.core.presentation.log.MyLog;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newFragment(){
        return new SettingsFragment();
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.preferences);
        findPreference("pref_key_reset_db").setOnPreferenceClickListener(
                preference -> {
                    showClearDbConfirmDialog();
                    return true;
                }
        );

    }

    private void showClearDbConfirmDialog() {

        MyLog.add("-- showClearDbConfirmDialog");

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.confirm_dialog_title)
                .setCancelable(true)
                .setMessage(R.string.clear_db_confirmation_msg)
                .setPositiveButton(R.string.btn_ok, (dialogInterface, i) ->
                {
                    showClearDbDialog();
                    dialogInterface.dismiss();
                })
                .show();
    }


    private void showClearDbDialog() {

        MyLog.add("-- showClearDbDialog");

        ClearDbDialogFragment.newFragment().show(
                getFragmentManager(), ClearDbDialogFragment.TAG
        );
    }

}
