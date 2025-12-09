package ru.samtakoy.download.data

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Url

internal interface FileApi {
    @GET("")
    suspend fun downloadFile(@Url url: String): ByteArray
}