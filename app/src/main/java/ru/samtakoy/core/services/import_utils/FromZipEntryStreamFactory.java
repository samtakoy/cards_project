package ru.samtakoy.core.services.import_utils;

import android.content.ContentResolver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ru.samtakoy.core.business.impl.ExportConst;
import ru.samtakoy.core.screens.log.MyLog;

public class FromZipEntryStreamFactory implements StreamFactory{

    private byte[] mData;
    private String mSrcPath;
    private String mFileName;
    private Long mThemeId;
    private ContentResolver mResolver;

    //private HashMap<String, Long> mThemeIdCache;

    public FromZipEntryStreamFactory(
            ZipInputStream zin,
            ZipEntry ze,
            ContentResolver resolver
    ) throws IOException {

        mResolver = resolver;
        mThemeId = 0L;

        ByteArrayOutputStream baOutStream = new ByteArrayOutputStream();
        for (int c = zin.read(); c != -1; c = zin.read()) {
            baOutStream.write(c);
        }
        baOutStream.close();
        mData = baOutStream.toByteArray();


        // _export/moxy/11.txt
        if(ze.getName().indexOf(ExportConst.EXPORT_ROOT_FOLDER) == 0){
            mSrcPath = ze.getName().substring(ExportConst.EXPORT_ROOT_FOLDER.length()+1);
        } else {
            mSrcPath = ze.getName();
        }

        mFileName = ze.getName().substring(ze.getName().lastIndexOf('/')+1);
        /*if(mFileName.indexOf(".") > 0){
            mFileName = mFileName.substring(0, mFileName.lastIndexOf("."));
        }/**/

        //mThemeIdCache = new HashMap<>();
    }

    public List getThemesPath(){

        String[] arrStrings = mSrcPath.split("/");
        int n = arrStrings.length-1;
        ArrayList<String> result = new ArrayList<>(n);

        for(int i=0; i<n; i++){
            if(arrStrings[i].length() > 0){
                result.add(arrStrings[i]);
            }
        }
        return result;
    }

    //public

    @Override
    public InputStream openStream() throws Exception {

        MyLog.add("++++++++++++++++++++++");
        MyLog.add("++ openStream: "+mData.length+", "+mSrcPath);


        return new ByteArrayInputStream(mData);
    }

    @Override
    public String getSrcPath() {
        return mSrcPath;
    }

    @Override
    public Long getThemeId() {
        return mThemeId;
    }

    public void setThemeId(Long themeId){
        mThemeId = themeId;
    }

    @Override
    public String getFileName() {
        return mFileName;
    }

    @Override
    public ContentResolver getResolver() {
        return mResolver;
    }



}
