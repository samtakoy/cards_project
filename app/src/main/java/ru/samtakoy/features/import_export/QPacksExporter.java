package ru.samtakoy.features.import_export;

import io.reactivex.Completable;
import ru.samtakoy.core.database.room.entities.QPackEntity;

public interface QPacksExporter {

    //void exportThemeTree(QPack qPack);
    boolean exportQPackToEmail(QPackEntity qPack);

    boolean exportQPack(QPackEntity qPack);


    Completable exportAllToEmail();

    Completable exportAllToFolder(String exportDirPath);

}
