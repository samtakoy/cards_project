package ru.samtakoy.features.import_export

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.gson.Gson
import ru.samtakoy.features.learncourse.data.CoursesRepository
import ru.samtakoy.core.presentation.log.MyLog.add
import ru.samtakoy.features.import_export.helpers.SendEmailHelper
import ru.samtakoy.features.learncourse.domain.model.LearnCourse
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.Writer
import javax.inject.Inject

class CoursesExporterImpl @Inject constructor(
    private val mContext: Context,
    private val mCoursesReposithory: CoursesRepository
) : CoursesExporter {

    override suspend fun exportAllToEmail() {
        exportCoursesToEmail(mCoursesReposithory.getAllCourses())
    }

    private fun serializeCourses(learnCourses: List<LearnCourse>): String {
        return Gson().toJson(learnCourses)
    }

    fun exportCoursesToFolder(learnCourses: MutableList<LearnCourse>, exportDirPath: String): Boolean {
        val file = File(exportDirPath, ExportConst.COURSES_FILE_NAME)
        if (file.exists()) {
            file.delete()
        }
        return exportCoursesToFile(learnCourses, file)
    }

    fun exportCoursesToFile(learnCourses: List<LearnCourse>, file: File): Boolean {
        var writer: Writer? = null
        try {
            writer = OutputStreamWriter(FileOutputStream(file), ExportConst.FILES_CHARSET)
            writer.write(serializeCourses(learnCourses))
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
                return false
            }
        }
        return true
    }

    fun exportCoursesToEmail(learnCourses: List<LearnCourse>): Boolean {
        var cacheDir = mContext.getCacheDir()
        cacheDir = File(cacheDir, "_send")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        val file: File?

        try {
            file = File.createTempFile("courses", ".txt", cacheDir)
            exportCoursesToFile(learnCourses, file)
            sendFile(FileProvider.getUriForFile(mContext, ExportConst.FILE_PROVIDER_AUTHORITY, file))
        } catch (e: Exception) {
            add(e.toString())
            return false
        }
        return true
    }

    private fun sendFile(fileUri: Uri?) {
        SendEmailHelper.sendFileByEmail(mContext, "all courses", "", fileUri)
    }
}
