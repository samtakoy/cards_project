package ru.samtakoy.core.services.import_utils;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.InputStream;

import ru.samtakoy.core.database.UriUtils;

public class FromUriStreamFactory implements StreamFactory{


    private ContentResolver mResolver;
    private Long mTargetThemeId;
    private Uri mSelectedFileUri;

    public FromUriStreamFactory(
            ContentResolver resolver, Long targetThemeId, Uri selectedFileUri
    ){

        mResolver = resolver;
        mTargetThemeId = targetThemeId;
        mSelectedFileUri = selectedFileUri;
    }

    @Override
    public InputStream openStream() throws Exception {
        return mResolver.openInputStream(mSelectedFileUri);
    }

    @Override
    public String getSrcPath() {
        return mSelectedFileUri.getPath();
    }

    @Override
    public Long getThemeId() {
        return mTargetThemeId;
    }

    @Override
    public String getFileName() {
        return UriUtils.getFileNameByUri(mResolver, mSelectedFileUri);
    }

    @Override
    public ContentResolver getResolver() {
        return mResolver;
    }
}
