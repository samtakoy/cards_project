package ru.samtakoy.core.presentation;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class DialogHelper {


    public static void showYesNoDialog(
            Context context,
            String title,
            String message,
            DialogInterface.OnClickListener okListener,
            @Nullable DialogInterface.OnClickListener noListener
    ){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, okListener)
                .setNegativeButton(android.R.string.no, noListener).show();
    }

}
