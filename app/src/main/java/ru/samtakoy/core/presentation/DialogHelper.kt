package ru.samtakoy.core.presentation

import android.R
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object DialogHelper {
    fun showYesNoDialog(
        context: Context,
        title: String,
        message: String,
        okListener: DialogInterface.OnClickListener,
        noListener: DialogInterface.OnClickListener?
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(R.drawable.ic_dialog_alert)
            .setPositiveButton(R.string.yes, okListener)
            .setNegativeButton(R.string.no, noListener).show()
    }
}
