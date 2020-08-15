package ru.samtakoy.core.services.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.samtakoy.core.services.api.pojo.RemoteFilesInfo;

public interface RequestApi {

    @GET("info.txt")
    Observable<RemoteFilesInfo> getFilesInfo();

}
