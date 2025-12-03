package ru.samtakoy.oldlegacy.features.import_export.helpers

import android.os.Build
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import ru.samtakoy.common.utils.log.MyLog.add
import ru.samtakoy.oldlegacy.features.import_export.utils.streams.FromZipEntryStreamFactory
import ru.samtakoy.oldlegacy.features.import_export.utils.isPackFile
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object ZipHelper {
    fun unzipStream(
        zippedFileInputStream: InputStream
    ): Observable<FromZipEntryStreamFactory> {
        return Observable.create<FromZipEntryStreamFactory>(ObservableOnSubscribe { emitter: ObservableEmitter<FromZipEntryStreamFactory> ->
            var zin: ZipInputStream? = null
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    zin = ZipInputStream(zippedFileInputStream, Charset.forName("Cp437"))
                } else {
                    zin = ZipInputStream(zippedFileInputStream)
                }

                var ze: ZipEntry?

                while ((zin.getNextEntry().also { ze = it }) != null) {
                    //create dir if required while unzipping

                    if (ze!!.isDirectory()) {
                        // do nothing
                    } else {
                        val fileName = ze.getName().substring(ze.getName().lastIndexOf('/') + 1)
                        if (isPackFile(fileName)) {
                            add("f:" + fileName)
                            emitter.onNext(FromZipEntryStreamFactory(zin, ze))
                            add("ok!")
                        }
                        zin.closeEntry()
                    }
                }

                zin.close()
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
                zin!!.close()
            }
        })
    }

    @Throws(IOException::class)
    fun zipDirectory(dir: File, zipFile: File?) {
        val fout = FileOutputStream(zipFile)
        val zout = ZipOutputStream(BufferedOutputStream(fout))
        try {
            zipSubDirectory("", dir, zout)
        } catch (e: IOException) {
            throw e
        } finally {
            zout.close()
        }
    }

    @Throws(IOException::class) private fun zipSubDirectory(basePath: String?, dir: File, zout: ZipOutputStream) {
        val BUFFER = 4096
        val buffer = ByteArray(BUFFER)
        val files = dir.listFiles()
        for (file in files!!) {
            if (file.isDirectory()) {
                val path = basePath + file.getName() + "/"
                zout.putNextEntry(ZipEntry(path))
                zipSubDirectory(path, file, zout)
                zout.closeEntry()
            } else {
                val fin = BufferedInputStream(FileInputStream(file), BUFFER)

                try {
                    zout.putNextEntry(ZipEntry(basePath + file.getName()))
                    var length: Int
                    while ((fin.read(buffer).also { length = it }) > 0) {
                        zout.write(buffer, 0, length)
                    }
                    zout.closeEntry()
                } catch (e: IOException) {
                    throw e
                } finally {
                    fin.close()
                }
            }
        } // for
    }
}
