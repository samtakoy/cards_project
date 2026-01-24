package ru.samtakoy.download.data

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.write
import ru.samtakoy.download.domain.FileRepository

internal class FileRepositoryImpl(
    private val fileApi: FileApi
) : FileRepository {

    override suspend fun downloadToTempFile(url: String): PlatformFile {
        val tempFile = PlatformFile("${FileKit.cacheDir}/$TEMP_FILE_NAME")
        if (tempFile.exists()) {
            tempFile.delete()
        }
        tempFile.write(fileApi.downloadFile(url))
        return tempFile
    }

    companion object {
        private const val TEMP_FILE_NAME = "cards_download.tmp"
    }
}