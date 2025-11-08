package ru.samtakoy.core.data.network

import io.reactivex.Observable
import retrofit2.http.GET
import ru.samtakoy.core.data.network.pojo.RemoteFilesInfo

interface RequestApi {
    @get:GET("info.txt")
    val filesInfo: Observable<RemoteFilesInfo>
}
