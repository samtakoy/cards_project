package ru.samtakoy.features.import_export;

import android.content.Context;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import io.reactivex.Completable;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.presentation.log.MyLog;
import ru.samtakoy.features.import_export.helpers.SendEmailHelper;


public class CoursesExporterImpl implements CoursesExporter {


    private Context mContext;
    private CoursesRepository mCoursesReposithory;

    public CoursesExporterImpl(Context ctx, CoursesRepository coursesRep) {
        mContext = ctx;
        mCoursesReposithory = coursesRep;
    }

    @Override
    public Completable exportAllToFolder(String exportDirPath) {
        mCoursesReposithory.getAllCoursesSingle()
                .map(learnCourses -> exportCoursesToFolder(learnCourses, exportDirPath))
                ;
        return null;
    }

    @Override
    public Completable exportAllToEmail() {
        return mCoursesReposithory.getAllCoursesSingle()
                //.toList()
                .map(learnCourses -> exportCoursesToEmail(learnCourses))
                .ignoreElement()
                ;
    }

    private String serializeCourses(List<LearnCourseEntity> learnCourses) {
        return new Gson().toJson(learnCourses);
    }

    public boolean exportCoursesToFolder(List<LearnCourseEntity> learnCourses, String exportDirPath) {
        File file = new File(exportDirPath, ExportConst.COURSES_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        return exportCoursesToFile(learnCourses, file);
    }

    public boolean exportCoursesToFile(List<LearnCourseEntity> learnCourses, File file) {

        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), ExportConst.FILES_CHARSET);
            writer.write(serializeCourses(learnCourses));
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch(Exception e){
                MyLog.add(e.toString());
                return false;
            }
        }
        return true;
    }

    public boolean exportCoursesToEmail(List<LearnCourseEntity> learnCourses) {

        File cacheDir = mContext.getCacheDir();
        cacheDir = new File(cacheDir, "_send");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        File file;

        try {
            file = File.createTempFile("courses", ".txt", cacheDir);
            exportCoursesToFile(learnCourses, file);
            sendFile(FileProvider.getUriForFile(mContext, ExportConst.FILE_PROVIDER_AUTHORITY, file));
        } catch (Exception e){
            MyLog.add(e.toString());
            return false;
        }
        return  true;
    }

    private void sendFile(Uri fileUri) {
        SendEmailHelper.sendFileByEmail( mContext, "all courses", "", fileUri );
    }

}
