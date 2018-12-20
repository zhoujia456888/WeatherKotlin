package com.jloveh.weatherkotlin.retrofit

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.jloveh.weatherkotlin.BuildConfig
import com.jloveh.weatherkotlin.constant.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient private constructor() {
    lateinit var service: ApiService
    lateinit var locationService:ApiService

    val DEFAULT_TIMEOUT: Long = 30
    lateinit var logging: HttpLoggingInterceptor


    private object Holder {
        val INSTANCE = ApiClient()
    }

    companion object {
        val instance by lazy { Holder.INSTANCE }
    }

    fun init() {

        if (BuildConfig.DEBUG) {
            logging = HttpLoggingInterceptor(
                    HttpLoggingInterceptor.Logger {
                        if (StringUtils.isEmpty(it)) return@Logger

                        var s: String = it.substring(0, 1)
                        /*if ("{" == s || "[" == s)
                            LogUtils.json(it)*/
                    }
            )
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(LoggingInterceptor())
                .addInterceptor(logging)
                .build()


        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        service = retrofit.create(ApiService::class.java)


        val locationRetrofit = Retrofit.Builder()
            .baseUrl(Constant.Location_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        locationService = locationRetrofit.create(ApiService::class.java)


    }
}