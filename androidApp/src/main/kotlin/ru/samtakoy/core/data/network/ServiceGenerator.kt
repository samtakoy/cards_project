package ru.samtakoy.core.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// TODO move to di
object ServiceGenerator {
    private const val BASE_URL = "https://raw.githubusercontent.com/samtakoy/data/master/"

    private val sHttpClient = OkHttpClient.Builder()

    private val sRetroftBuilder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    private fun createRetrofit(): Retrofit {
        return sRetroftBuilder.client(sHttpClient.build()).build()
    }

    val api: RequestApi = createRetrofit().create<RequestApi>(RequestApi::class.java)
}

