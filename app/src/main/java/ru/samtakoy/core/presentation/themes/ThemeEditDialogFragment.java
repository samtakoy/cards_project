package ru.samtakoy.core.presentation.themes;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.samtakoy.R;

public class ThemeEditDialogFragment extends DialogFragment {

    public static final String RESULT_EXTRA_TEXT = "RESULT_EXTRA_TEXT";
    private static final String ARG_TEXT = "ARG_TEXT";

    private EditText mInputText;

    public static ThemeEditDialogFragment newDialog(String defaultThemeName){
        ThemeEditDialogFragment result = new ThemeEditDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, defaultThemeName);
        result.setArguments(args);
        return result;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        String defaultThemeName = args.getString(ARG_TEXT);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_theme_add, null);
        mInputText = (EditText)v.findViewById(R.id.fragment_dialog_theme_add_input);
        mInputText.setText(defaultThemeName);
        //mInputText.requestFocus();

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                trySendResult();
                            }
                        }
                )
                .create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void trySendResult(){
        if(getTargetFragment() == null){
            return;
        }
        Intent result = new Intent();
        result.putExtra(RESULT_EXTRA_TEXT, mInputText.getText().toString().trim());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
    }
}
