package com.example.nestixbook.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "https://www.googleapis.com/"
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor{chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .method(original.method(), original.body())

        val request = requestBuilder.build()
        chain.proceed(request)
    }.build()

object GoogleRetrofitClient {

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(Api::class.java)
    }
}