package ru.samtakoy.features.import_export.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.samtakoy.features.import_export.ExportConst

object SendEmailHelper {
    fun sendFileByEmail(
        ctx: Context,
        subject: String,
        text: String?,
        fileUri: Uri?
    ) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        // set the type to 'email'
        emailIntent.setType(ExportConst.EMAIL_CONTENT_TYPE)
        val to: Array<String> = arrayOf<String>(ExportConst.DEFAULT_EMAIL)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
        //emailIntent .putExtra(Intent.EXTRA_NA, to);
        // the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        // the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, text)
        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val chooserIntent = Intent.createChooser(emailIntent, "Send email...")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        ctx.startActivity(chooserIntent)
    }
}
