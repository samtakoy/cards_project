package ru.samtakoy.oldlegacy.features.import_export.utils.streams

import android.content.ContentResolver
import android.net.Uri
import ru.samtakoy.oldlegacy.features.import_export.utils.UriUtils
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
        get() = UriUtils.getFileNameByUri(mResolver, mSelectedFileUri)
}
