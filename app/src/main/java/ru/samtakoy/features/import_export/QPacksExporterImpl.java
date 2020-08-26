package ru.samtakoy.features.import_export;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.QPacksRepository;
import ru.samtakoy.core.business.ThemesRepository;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.database.room.entities.ThemeEntity;
import ru.samtakoy.core.database.room.entities.other.CardWithTags;
import ru.samtakoy.core.screens.log.MyLog;
import ru.samtakoy.core.utils.DateUtils;
import ru.samtakoy.core.utils.FileUtils;
import ru.samtakoy.features.import_export.helpers.QPackExportHelper;
import ru.samtakoy.features.import_export.helpers.SendEmailHelper;
import ru.samtakoy.features.import_export.helpers.ZipHelper;


public class QPacksExporterImpl implements QPacksExporter {


    private static final String SEND_FOLDER = "_send";
    private static final String ZIP_FOLDER = "_zip";

    @Inject
    Context mContext;
    @Inject
    CardsRepository mCardsRepository;
    @Inject
    QPacksRepository mQPacksRepository;
    @Inject
    ThemesRepository mThemesRepository;

    @Inject
    public QPacksExporterImpl() {
    }

    private String getExportThemeLocalPath(Long themeId) {

        StringBuilder sb = new StringBuilder();
        while (themeId > 0) {
            ThemeEntity theme = mThemesRepository.getTheme(themeId);
            sb.insert(0, theme.getTitle());
            sb.insert(0, "/");
            themeId = theme.getParentId();
        }
        sb.insert(0, ExportConst.EXPORT_ROOT_FOLDER);
        sb.insert(0, "/");

        return sb.toString();
    }

    public String getExportQPackLocalPath(QPackEntity qPack) {
        return getExportThemeLocalPath(qPack.getThemeId());
    }

    /*
    public void exportThemeTree(QPack qPack){
        String localPath = getExportQPackLocalPath(qPack);
        validateThemePath(localPath);
    }/**/

    @Nullable
    private String validateThemePath(String baseFolder, String localPath) {

        File file = new File(baseFolder, localPath);
        if(!file.exists()) {
            file.mkdirs();
        }

        if(!file.exists()){
            MyLog.add("-- file not created:"+baseFolder+", "+localPath);
            return null;
        }

        return file.getAbsolutePath();
    }


    public boolean exportQPackToEmail(QPackEntity qPack) {

        List<CardWithTags> cards = mCardsRepository.getQPackCardsWithTags(qPack.getId());

        File file;

        try {
            file = File.createTempFile("qpack", ".txt", recreateTempFolderFile(SEND_FOLDER));
            exportQPackToFile(file, qPack, cards);
            sendQPackFile(FileProvider.getUriForFile(mContext, ExportConst.FILE_PROVIDER_AUTHORITY, file), qPack);
        } catch (Exception e) {
            MyLog.add(e.toString());
            return false;
        }
        return  true;
    }

    private String getCurrentTimeString(){
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return fmtOut.format(DateUtils.getCurrentTimeDate());
    }

    private void sendQPackFile(Uri fileUri, QPackEntity qPack) {
        SendEmailHelper.sendFileByEmail(
                mContext,
                "qpack: " + qPack.getTitle() + " (" + getCurrentTimeString() + ")",
                "desc: " + qPack.getDesc(),
                fileUri
        );
    }

    public boolean exportQPack(QPackEntity qPack) {
        String defaultBaseFolder = mContext.getExternalFilesDir(null).getAbsolutePath();
        ;
        return exportQPackToFolder(qPack, defaultBaseFolder);
    }

    public boolean exportQPackToFolder(QPackEntity qPack, String baseFolder) {

        MyLog.add("export qPack: " + qPack + ", to folder: " + baseFolder);

        String path = validateThemePath(baseFolder, getExportQPackLocalPath(qPack));

        if (path == null) {
            return false;
        }

        List<CardWithTags> cards = mCardsRepository.getQPackCardsWithTags(qPack.getId());

        // первый вариант макс. просто - в файл export.txt
        //getFile
        File file = new File(path, qPack.getExportFileName());

        // TODO подтверждение удаления
        if(file.exists()){
            file.delete();
        }

        return exportQPackToFile(file, qPack, cards);

    }


    private boolean exportQPackToFile(File file, QPackEntity qPack, List<CardWithTags> cards) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), ExportConst.FILES_CHARSET);
            QPackExportHelper.export(qPack, cards, writer);
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                MyLog.add(e.toString());
                return false;
            }
        }
        return true;
    }

    private File recreateTempFolderFile(String folderName) throws IOException {
        File sendTmpDir = mContext.getCacheDir();
        sendTmpDir = new File(sendTmpDir, folderName);
        if(sendTmpDir.exists()){
            FileUtils.deleteDirectoryRecursionJava6(sendTmpDir);
        }
        sendTmpDir.mkdirs();
        return sendTmpDir;
    }


    @Override
    public Completable exportAllToEmail() {

        final String exportDirPath;

        try {
            exportDirPath = recreateTempFolderFile(SEND_FOLDER).getAbsolutePath();
        } catch (IOException e) {
            return Completable.fromCallable(() -> {throw e;});
        }

        MyLog.add("__exportAllToEmail__, callThread:"+Thread.currentThread().getName());

        return exportAllToFolder(exportDirPath)
                .doOnComplete(
                () -> {
                    MyLog.add("__exportAllToEmail__, onComplete thread:"+Thread.currentThread().getName());


                    File zipFile = File.createTempFile("all_packs_export", ".zip", recreateTempFolderFile(ZIP_FOLDER));
                    ZipHelper.zipDirectory(new File(exportDirPath), zipFile);
                    Uri fileUri = FileProvider.getUriForFile(mContext, ExportConst.FILE_PROVIDER_AUTHORITY, zipFile);
                    SendEmailHelper.sendFileByEmail(mContext, "all qPacks archive", "", fileUri);
                }
        );
    }

    @Override
    public Completable exportAllToFolder(String exportDirPath) {

        return mQPacksRepository
                .getAllQPacks()
                .flatMap(list -> Flowable.fromIterable(list))
                .map(qPack -> {

                            if (exportQPackToFolder(qPack, exportDirPath)) {
                                return qPack;
                            } else {
                                throw new Exception("cant Export qPack:" + qPack.getId() + ", " + qPack.getTitle() + ", baseFolder: " + exportDirPath);
                            }
                        }
                )
                .ignoreElements();
    }




}

