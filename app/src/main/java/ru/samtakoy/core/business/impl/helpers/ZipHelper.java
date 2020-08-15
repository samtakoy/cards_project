package ru.samtakoy.core.business.impl.helpers;

import android.content.ContentResolver;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import io.reactivex.Observable;
import ru.samtakoy.core.screens.log.MyLog;
import ru.samtakoy.core.services.import_utils.FromZipEntryStreamFactory;
import ru.samtakoy.core.services.import_utils.ImportCardsHelper;

public class ZipHelper {

    public static Observable<FromZipEntryStreamFactory> unzipStream(
            ContentResolver resolver,
            InputStream zippedFileInputStream){

        return Observable.create(emitter -> {

            ZipInputStream zin = null;
            try {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    zin = new ZipInputStream(zippedFileInputStream, Charset.forName("Cp437"));
                }else{
                    zin = new ZipInputStream(zippedFileInputStream);
                }

                ZipEntry ze;

                while ((ze = zin.getNextEntry()) != null) {

                    //create dir if required while unzipping
                    if (ze.isDirectory()) {
                        // do nothing
                    } else {

                        String fileName = ze.getName().substring(ze.getName().lastIndexOf('/')+1);
                        if(ImportCardsHelper.isPackFile(fileName)) {
                            MyLog.add("f:"+fileName);
                            emitter.onNext(new FromZipEntryStreamFactory(zin, ze, resolver));
                            MyLog.add("ok!");
                        }
                        zin.closeEntry();
                    }
                }

                zin.close();
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
                zin.close();
            }
        });


    }

    public static void zipDirectory(File dir, File zipFile) throws IOException {

        FileOutputStream fout = new FileOutputStream(zipFile);
        ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(fout));
        try {
            zipSubDirectory("", dir, zout);
        }catch (IOException e){ throw e; }
        finally { zout.close(); }
    }

    private static void zipSubDirectory(String basePath, File dir, ZipOutputStream zout) throws IOException {

        final int BUFFER = 4096;
        byte[] buffer = new byte[BUFFER];
        File[] files = dir.listFiles();
        for (File file : files) {

            if (file.isDirectory()) {

                String path = basePath + file.getName() + "/";
                zout.putNextEntry(new ZipEntry(path));
                zipSubDirectory(path, file, zout);
                zout.closeEntry();
            } else {

                BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file), BUFFER);

                try {
                    zout.putNextEntry(new ZipEntry(basePath + file.getName()));
                    int length;
                    while ((length = fin.read(buffer)) > 0) {
                        zout.write(buffer, 0, length);
                    }
                    zout.closeEntry();
                }
                catch (IOException e){ throw e; }
                finally {
                    fin.close();
                }

            }
        } // for
    }



}
