package ru.samtakoy.core.services.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RemoteFile {


    @SerializedName("mPath")
    @Expose
    private String mPath;
    @SerializedName("mVersion")
    @Expose
    private Integer mVersion;
    @SerializedName("anchor")
    @Expose
    private Integer mAnchor;

    public String getPath(){ return mPath; }
    public Integer getVersion() { return mVersion; }
    public Integer getAnchor() { return mAnchor; }
}
