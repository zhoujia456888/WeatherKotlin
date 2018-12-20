package com.jloveh.weatherkotlin.retrofit

import com.blankj.utilcode.util.LogUtils
import com.jloveh.weatherkotlin.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        var url = request.url()
        var connection = chain.connection()
        var headers = request.headers()
        if (BuildConfig.DEBUG) LogUtils.d("发送请求:\n地址:$url \n连接:$connection  \n头部:$headers")
        return chain.proceed(request)
    }
}