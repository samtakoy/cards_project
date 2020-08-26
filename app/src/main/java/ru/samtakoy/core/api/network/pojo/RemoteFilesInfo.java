package ru.samtakoy.core.api.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemoteFilesInfo {

    @SerializedName("files")
    @Expose
    private List<RemoteFile> files = null;

    public List<RemoteFile> getFiles() {
        return files;
    }

    public void setFiles(List<RemoteFile> files) {
        this.files = files;
    }

}
