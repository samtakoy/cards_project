package ru.samtakoy.core.data.network.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RemoteFile(
    @SerializedName("mPath")
    @Expose
    val path: String? = null,
    @SerializedName("mVersion")
    @Expose
    val version: Int? = null,
    @SerializedName("anchor")
    @Expose
    val anchor: Int? = null
)
