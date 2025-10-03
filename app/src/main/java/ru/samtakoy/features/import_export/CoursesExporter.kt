package ru.samtakoy.features.import_export;

import io.reactivex.Completable;

public interface CoursesExporter {


    Completable exportAllToFolder(String exportDirPath);
    Completable exportAllToEmail();


}
