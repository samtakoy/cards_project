package ru.samtakoy.features.import_export.utils.streams

import ru.samtakoy.common.utils.MyLog.add
import ru.samtakoy.features.import_export.ExportConst
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class FromZipEntryStreamFactory(
    zin: ZipInputStream,
    ze: ZipEntry //,
    //ContentResolver resolver
) : StreamFactory {
    private val mData: ByteArray
    private var mSrcPath: String

    /*
    @Override
    public ContentResolver getResolver() {
        return mResolver;
    }*/
    override val fileName: String
    override var themeId: Long = 0L

    //private ContentResolver mResolver;
    //private HashMap<String, Long> mThemeIdCache;
    init {
        //mResolver = resolver;

        val baOutStream = ByteArrayOutputStream()
        var c = zin.read()
        while (c != -1) {
            baOutStream.write(c)
            c = zin.read()
        }
        baOutStream.close()
        mData = baOutStream.toByteArray()

        // _export/moxy/11.txt
        if (ze.getName().indexOf(ExportConst.EXPORT_ROOT_FOLDER) == 0) {
            mSrcPath = ze.getName().substring(ExportConst.EXPORT_ROOT_FOLDER.length + 1)
        } else {
            mSrcPath = ze.getName()
        }

        this.fileName = ze.getName().substring(ze.getName().lastIndexOf('/') + 1)

        /*if(mFileName.indexOf(".") > 0){
            mFileName = mFileName.substring(0, mFileName.lastIndexOf("."));
        }/ **/

        //mThemeIdCache = new HashMap<>();
    }

    val themesPath: MutableList<String>
        get() {
            val arrStrings: Array<String> =
                mSrcPath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val n = arrStrings.size - 1
            val result = ArrayList<String>(n)

            for (i in 0..<n) {
                if (arrStrings[i].length > 0) {
                    result.add(arrStrings[i])
                }
            }
            return result
        }

    //public
    @Throws(Exception::class)
    override fun openStream(): InputStream {
        add("++++++++++++++++++++++")
        add("++ openStream: " + mData.size + ", " + mSrcPath)


        return ByteArrayInputStream(mData)
    }

    override val srcPath: String
        get() = mSrcPath
}
