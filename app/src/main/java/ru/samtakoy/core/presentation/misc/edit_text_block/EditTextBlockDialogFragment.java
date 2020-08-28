package ru.samtakoy.core.presentation.misc.edit_text_block;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import ru.samtakoy.R;

public class EditTextBlockDialogFragment extends DialogFragment {

    public static final String TAG = "EditTextBlockDialogFrag";

    private static final String ARG_TEXT = "ARG_TEXT";
    public static final String RESULT_TEXT = "RESULT_TEXT";

    EditText mEditText;

    public static EditTextBlockDialogFragment newInstance(
            String text,
            Fragment targetFragment,
            int targetFragmentRequestCode
    ) {

        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        EditTextBlockDialogFragment fragment = new EditTextBlockDialogFragment();
        fragment.setArguments(args);
        fragment.setTargetFragment(targetFragment, targetFragmentRequestCode);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String text = getArguments().getString(ARG_TEXT);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_edit_text_block, null);

        mEditText = v.findViewById(R.id.edit_text);
        mEditText.setText(text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(v)
                .setCancelable(true)
                .setOnCancelListener(dialogInterface -> onCancel())
                .setPositiveButton(R.string.btn_save, (dialogInterface, i) -> onOk())
                .setNegativeButton(R.string.btn_cancel, (dialogInterface, i) -> onCancel());
        return builder.create();
    }

    private void onOk(){

        String resultText = mEditText.getText().toString();
        
        Intent result = new Intent();
        result.putExtra(RESULT_TEXT, resultText);


        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
        dismiss();
    }

    private void onCancel(){
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        dismiss();
    }

}
