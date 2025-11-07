package com.powluiz.forexwearos.data.api

import com.powluiz.forexwearos.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val API_KEY = BuildConfig.AWESOME_API_KEY
    private const val BASE_URL = "https://economia.awesomeapi.com.br/"

    init {
        if (API_KEY.isBlank() || API_KEY == "null") {
            throw IllegalStateException(
                "AWESOME_API_KEY não foi definida no arquivo local.properties!"
            )
        }
    }


    private val headerInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("x-api-key", API_KEY)
            .build()
        chain.proceed(newRequest)
    }


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val forexApiService: ForexApiService by lazy {
        retrofit.create(ForexApiService::class.java)
    }
}