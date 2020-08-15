package ru.samtakoy.core.services.import_utils;

import android.content.ContentResolver;

import java.io.InputStream;

public interface StreamFactory {

    InputStream openStream() throws Exception;
    String getSrcPath();
    Long getThemeId();
    String getFileName();
    ContentResolver getResolver();

}
