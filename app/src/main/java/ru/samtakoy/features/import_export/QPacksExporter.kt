package ru.samtakoy.features.import_export;

import io.reactivex.Completable;
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity;

public interface QPacksExporter {

    //void exportThemeTree(QPack qPack);
    Completable exportQPackToEmailRx(QPackEntity qPack);

    Completable exportQPack(QPackEntity qPack);


    Completable exportAllToEmail();

    Completable exportAllToFolder(String exportDirPath);

}
