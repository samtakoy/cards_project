package ru.samtakoy.core.data.network.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RemoteFilesInfo {
    @SerializedName("files")
    @Expose
    val files: List<RemoteFile>? = null
}
