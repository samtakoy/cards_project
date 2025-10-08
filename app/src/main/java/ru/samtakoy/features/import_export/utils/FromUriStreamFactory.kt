package ru.samtakoy.features.import_export.utils

import android.content.ContentResolver
import android.net.Uri
import ru.samtakoy.features.import_export.utils.UriUtils.getFileNameByUri
import java.io.InputStream

class FromUriStreamFactory(
    private val mResolver: ContentResolver,
    override val themeId: Long,
    private val mSelectedFileUri: Uri
) : StreamFactory {
    @Throws(Exception::class) override fun openStream(): InputStream {
        return mResolver.openInputStream(mSelectedFileUri)!!
    }

    override val srcPath: String
        get() = mSelectedFileUri.getPath()!!

    override val fileName: String
        get() = getFileNameByUri(mResolver, mSelectedFileUri)
}
