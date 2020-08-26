package ru.samtakoy.features.import_export.utils;

import java.io.InputStream;

public interface StreamFactory {

    InputStream openStream() throws Exception;
    String getSrcPath();
    Long getThemeId();
    String getFileName();
    //ContentResolver getResolver();

}
