package ru.samtakoy.core.api.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = "https://raw.githubusercontent.com/samtakoy/cards/master/";

    private static final OkHttpClient.Builder sHttpClient = new OkHttpClient.Builder();

    private static final Retrofit.Builder sRetroftBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    private static Retrofit createRetrofit(){
        return  sRetroftBuilder.client(sHttpClient.build()).build();
    }

    private static final RequestApi sRequestApi = createRetrofit().create(RequestApi.class);

    public static RequestApi getApi(){ return sRequestApi; }
}

