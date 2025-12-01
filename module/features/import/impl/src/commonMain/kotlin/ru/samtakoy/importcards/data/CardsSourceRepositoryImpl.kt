package ru.samtakoy.importcards.data

import com.oldguy.common.io.File
import com.oldguy.common.io.ZipEntry
import com.oldguy.common.io.ZipFile
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.samtakoy.importcards.data.model.ExportImportConst
import ru.samtakoy.importcards.data.model.QPackSource
import ru.samtakoy.importcards.domain.CardsSourceRepository

internal class CardsSourceRepositoryImpl : CardsSourceRepository {

    override suspend fun getFromZip(file: PlatformFile): Flow<QPackSource> {
        return flow {
            val tempFile = copyToTempZipFile(file)
            ZipFile(File(tempFile.path)).use { zip ->
                zip.entries.forEach { zipEntry ->
                    if (zipEntry.isValidQPackFile()) {
                        val srcPath = if (zipEntry.name.indexOf(ExportImportConst.EXPORT_ROOT_FOLDER) == 0) {
                            // удалить из пути имя служебной дирректории
                            zipEntry.name.substring(ExportImportConst.EXPORT_ROOT_FOLDER.length + 1)
                        } else {
                            zipEntry.name
                        }
                        emit(
                            QPackSource(
                                content = zip.readByteArray(zipEntry),
                                srcPath = srcPath,
                                parentThemeNames = getParentThemeNames(qPathPath = srcPath),
                                themeId = 0L,
                                fileName = zipEntry.name.substring(zipEntry.name.lastIndexOf('/') + 1)
                            )
                        )
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getParentThemeNames(qPathPath: String): List<String> {
        val arrStrings = qPathPath.split("/").map { it.trim() }.dropLastWhile { it.isEmpty() }
        return buildList {
            // удалить имя текущего файла, оставить только родительские темы
            arrStrings.dropLast(1).forEach { themeName ->
                add(themeName)
            }
        }
    }

    private suspend fun copyToTempZipFile(from: PlatformFile): PlatformFile {
        val tempFile = PlatformFile(FileKit.cacheDir, TEMP_FILE_NAME)
        if (tempFile.exists()) {
            tempFile.delete(mustExist = false)
        }
        tempFile.write(from)
        return tempFile
    }

    private fun ZipEntry.isDir(): Boolean {
        return name.endsWith("/")
    }

    private fun ZipEntry.isValidQPackFile(): Boolean {
        return name.endsWith(".txt")
    }

    private suspend fun ZipFile.readByteArray(zipEntry: ZipEntry): ByteArray {
        val buffer = mutableListOf<Byte>()
        this.readEntry(zipEntry) { entry: ZipEntry, content: ByteArray, count: UInt, last: Boolean ->
            if (count > 0U) buffer.addAll(content.take(count.toInt()))
        }
        return buffer.toByteArray()
    }

    companion object {
        private const val TEMP_FILE_NAME = "temporary.zip"
    }
}