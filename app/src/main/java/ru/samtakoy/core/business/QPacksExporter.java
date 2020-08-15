package ru.samtakoy.core.business;

import io.reactivex.Completable;
import ru.samtakoy.core.model.QPack;

public interface QPacksExporter {

    //void exportThemeTree(QPack qPack);
    boolean exportQPackToEmail(QPack qPack);
    boolean exportQPack(QPack qPack);

    Completable exportAllToEmail();
    Completable exportAllToFolder(String exportDirPath);

}
