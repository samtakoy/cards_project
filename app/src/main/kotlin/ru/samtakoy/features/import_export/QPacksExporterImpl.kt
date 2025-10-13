package ru.samtakoy.features.import_export

import android.content.Context
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.core.app.utils.FileUtils
import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.data.qpack.QPacksRepository
import ru.samtakoy.data.theme.ThemesRepository
import ru.samtakoy.common.utils.MyLog.add
import ru.samtakoy.domain.card.domain.model.CardWithTags
import ru.samtakoy.features.import_export.helpers.QPackExportHelper
import ru.samtakoy.features.import_export.helpers.SendEmailHelper
import ru.samtakoy.features.import_export.helpers.ZipHelper
import ru.samtakoy.domain.qpack.QPack
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.text.SimpleDateFormat
import javax.inject.Inject

class QPacksExporterImpl @Inject constructor(
    private val mContext: Context,
    private val mCardsRepository: CardsRepository,
    private val mQPacksRepository: QPacksRepository,
    private val mThemesRepository: ThemesRepository
) : QPacksExporter {

    @WorkerThread
    override suspend fun exportQPackToEmail(qPackId: Long) {
        exportQPackToEmail(
            qPack = mQPacksRepository.getQPack(qPackId)!!,
            cards = mCardsRepository.getQPackCardsWithTags(qPackId)
        )
    }

    @WorkerThread
    override suspend fun exportQPack(qPack: QPack) {
        val defaultBaseFolder = mContext.getExternalFilesDir(null)!!.getAbsolutePath()
        exportQPackToFolder(qPack, defaultBaseFolder)
    }

    @WorkerThread
    override suspend fun exportAllToEmail() {
        // with
        add("__exportAllToEmail__")
        val exportDirPath: String = recreateTempFolderFile(SEND_FOLDER).getAbsolutePath()
        exportAllToFolder(exportDirPath)
        val zipFile = File.createTempFile("all_packs_export", ".zip", recreateTempFolderFile(ZIP_FOLDER))
        ZipHelper.zipDirectory(File(exportDirPath), zipFile)
        val fileUri = FileProvider.getUriForFile(mContext, ExportConst.FILE_PROVIDER_AUTHORITY, zipFile)
        SendEmailHelper.sendFileByEmail(mContext, "all qPacks archive", "", fileUri)
    }

    @WorkerThread
    override suspend fun exportAllToFolder(exportDirPath: String) {
        mQPacksRepository.getAllQPacks().forEach { qPack ->
            exportQPackToFolder(qPack, exportDirPath)
        }
    }

    private suspend fun getExportThemeLocalPath(themeId: Long): String {
        var themeId = themeId
        val sb = StringBuilder()
        while (themeId > 0) {
            val theme = mThemesRepository.getTheme(themeId)!!
            sb.insert(0, theme.title)
            sb.insert(0, "/")
            themeId = theme.parentId
        }
        sb.insert(0, ExportConst.EXPORT_ROOT_FOLDER)
        sb.insert(0, "/")

        return sb.toString()
    }

    suspend fun getExportQPackLocalPath(qPack: QPack): String {
        return getExportThemeLocalPath(qPack.themeId)
    }

    private fun validateThemePath(baseFolder: String, localPath: String): String {
        val file = File(baseFolder, localPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.getAbsolutePath()
    }

    private fun exportQPackToEmail(qPack: QPack, cards: List<CardWithTags>): Boolean {
        val file: File?

        try {
            file = File.createTempFile("qpack", ".txt", recreateTempFolderFile(SEND_FOLDER))
            exportQPackToFile(
                file = file,
                qPack = qPack,
                cards = cards
            )
            sendQPackFile(
                FileProvider.getUriForFile(mContext, ExportConst.FILE_PROVIDER_AUTHORITY, file),
                qPack
            )
        } catch (e: Exception) {
            add(e.toString())
            return false
        }
        return true
    }

    private val currentTimeString: String
        get() {
            val fmtOut = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            return fmtOut.format(DateUtils.currentTimeDate)
        }

    private fun sendQPackFile(fileUri: Uri?, qPack: QPack) {
        SendEmailHelper.sendFileByEmail(
            mContext,
            "qpack: " + qPack.title + " (" + this.currentTimeString + ")",
            "desc: " + qPack.desc,
            fileUri
        )
    }

    private suspend fun exportQPackToFolder(qPack: QPack, baseFolder: String) {
        add("export qPack: " + qPack.title + ", to folder: " + baseFolder)

        val path = validateThemePath(baseFolder, getExportQPackLocalPath(qPack))
        // первый вариант макс. просто - в файл export.txt
        val file = File(path, qPack.getExportFileName())
        if (file.exists()) {
            file.delete()
        }

        exportQPackToFile(
            file = file,
            qPack = qPack,
            cards = mCardsRepository.getQPackCardsWithTags(qPack.id)
        )
    }

    private fun exportQPackToFile(
        file: File,
        qPack: QPack,
        cards: List<CardWithTags>
    ): Boolean {
        var writer: Writer? = null
        try {
            writer = OutputStreamWriter(FileOutputStream(file), ExportConst.FILES_CHARSET)
            QPackExportHelper.export(qPack, cards, writer)
        } catch (e: Exception) {
            return false
        } finally {
            try {
                if (writer != null) {
                    writer.flush()
                    writer.close()
                }
            } catch (e: Exception) {
                add(e.toString())
            }
        }

        add("exportQPackToFile !OK! qPack: " + qPack.title + ", to folder: " + file.getAbsolutePath())
        return true
    }

    @Throws(IOException::class)
    private fun recreateTempFolderFile(folderName: String): File {
        var sendTmpDir = mContext.getCacheDir()
        sendTmpDir = File(sendTmpDir, folderName)
        if (sendTmpDir.exists()) {
            FileUtils.deleteDirectoryRecursionJava6(sendTmpDir)
        }
        sendTmpDir.mkdirs()
        return sendTmpDir
    }


    private fun QPack.getExportFileName(): String {
        val result: String = if (fileName.length > 0) fileName else id.toString()
        return if (result.indexOf('.') < 0) "$result.txt" else result
    }

    companion object {
        private const val SEND_FOLDER = "_send"
        private const val ZIP_FOLDER = "_zip"
    }
}

