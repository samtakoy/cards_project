package ru.samtakoy.core.business.impl;

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
import ru.samtakoy.core.business.CoursesExporter;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.impl.helpers.SendEmailHelper;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.screens.log.MyLog;


public class CoursesExporterImpl implements CoursesExporter {


    private Context mContext;
    private CoursesRepository mCoursesReposithory;

    public CoursesExporterImpl(Context ctx, CoursesRepository coursesRep) {
        mContext = ctx;
        mCoursesReposithory = coursesRep;
    }

    @Override
    public Completable exportAllToFolder(String exportDirPath) {
        mCoursesReposithory.getAllCourses()
                .toList()
                .map(learnCourses -> exportCoursesToFolder(learnCourses, exportDirPath))
                ;
        return null;
    }

    @Override
    public Completable exportAllToEmail() {
        return mCoursesReposithory.getAllCourses()
                .toList()
                .map(learnCourses -> exportCoursesToEmail(learnCourses))
                .ignoreElement()
        ;
    }

    private String serializeCourses(List<LearnCourse> learnCourses){
        return new Gson().toJson(learnCourses);
    }

    public boolean exportCoursesToFolder(List<LearnCourse> learnCourses, String exportDirPath){
        File file = new File(exportDirPath, ExportConst.COURSES_FILE_NAME);
        if(file.exists()){ file.delete(); }
        return exportCoursesToFile(learnCourses, file);
    }

    public boolean exportCoursesToFile(List<LearnCourse> learnCourses, File file){

        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), ExportConst.FILES_CHARSET);
            writer.write(serializeCourses(learnCourses));
        }catch(Exception e){
            return false;
        }finally {
            try {
                if(writer != null){writer.flush();writer.close();}
            } catch(Exception e){
                MyLog.add(e.toString());
                return false;
            }
        }
        return true;
    }

    public boolean exportCoursesToEmail(List<LearnCourse> learnCourses){

        File cacheDir = mContext.getCacheDir();
        cacheDir = new File(cacheDir, "_send");
        if(!cacheDir.exists()){
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
