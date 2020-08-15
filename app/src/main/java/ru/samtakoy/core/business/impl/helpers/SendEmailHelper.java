package ru.samtakoy.core.business.impl.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import ru.samtakoy.core.business.impl.ExportConst;

public class SendEmailHelper {



    public static void sendFileByEmail(
            Context ctx,
            String subject,
            String text,
            Uri fileUri
    ) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // set the type to 'email'
        emailIntent.setType(ExportConst.EMAIL_CONTENT_TYPE);
        String to[] = {ExportConst.DEFAULT_EMAIL};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        //emailIntent .putExtra(Intent.EXTRA_NA, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, fileUri);
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent .putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent chooserIntent = Intent.createChooser(emailIntent , "Send email...");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        ctx.startActivity(chooserIntent);
    }

}
