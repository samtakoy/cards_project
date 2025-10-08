package ru.samtakoy.features.import_export.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore

object UriUtils {
    @JvmStatic fun getFileNameByUri(contentResolver: ContentResolver, uri: Uri): String {
        var fileName = "file"
        var filePathUri = uri

        if (uri.getScheme()!!.compareTo("file") == 0) {
            fileName = filePathUri.getLastPathSegment().toString()
        } else if (uri.getScheme().toString().compareTo("content") == 0) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val column_index =
                    cursor.getColumnIndex(MediaStore.Images.Media.DATA) //Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                if (column_index < 0) {
                    return ""
                }
                filePathUri = Uri.parse(cursor.getString(column_index))
                fileName = filePathUri.getLastPathSegment().toString()
            }
        } else {
            fileName = fileName + filePathUri.getLastPathSegment()
        }
        return fileName
    }
}
