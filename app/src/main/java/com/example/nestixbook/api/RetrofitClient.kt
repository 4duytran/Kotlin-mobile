package com.example.nestixbook.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// api key
private const val API_KEY="To1jfCxtNEFjI0FKOqaP6FKV6MkkB8PokexKfo"
// url api site
private const val BASE_URL = "https://sduytran.fr/realisation/api/"
// Using okHttpclient to add info into request header
private val okHttpClient = OkHttpClient.Builder() // Build okhttp client
    .addInterceptor{chain ->
        val original = chain.request() // Build chain
        val requestBuilder = original.newBuilder()
            .header("appi", API_KEY) // add API key to header
            .method(original.method(), original.body())


        val request = requestBuilder.build()
        chain.proceed(request)
    }.build()

// Build retrofit client
object RetrofitClient {
    val instance: Api by lazy {
        val retrofit = Retrofit.Builder() // build retrofit object
            .baseUrl(BASE_URL) // Init url
            .addConverterFactory(GsonConverterFactory.create()) // use GsonConverterFactory for convert to object
            .client(okHttpClient) // build client
            .build()
        // create object by using Java class API
        retrofit.create(Api::class.java)
    }
}