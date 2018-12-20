package com.jloveh.weatherkotlin.retrofit

import com.jloveh.weatherkotlin.bean.LocationBean
import com.jloveh.weatherkotlin.bean.WeatherBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("find?")
    fun getLocation(@Query("location") location: String, @Query("key") key: String): Observable<LocationBean>





    @GET("weather?")
    fun getForecast(@Query("location") location: String, @Query("key") key: String): Observable<WeatherBean>


}