package ru.samtakoy.core.data.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.samtakoy.core.data.network.pojo.RemoteFilesInfo;

public interface RequestApi {

    @GET("info.txt")
    Observable<RemoteFilesInfo> getFilesInfo();

}
