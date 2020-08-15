package ru.samtakoy.core.business;

import io.reactivex.Completable;

public interface CoursesExporter {


    Completable exportAllToFolder(String exportDirPath);
    Completable exportAllToEmail();


}
