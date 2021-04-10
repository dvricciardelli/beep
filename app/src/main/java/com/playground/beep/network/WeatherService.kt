package com.playground.beep.network

import com.playground.beep.models.WeatherResponse
import retrofit.http.GET
import retrofit.http.Query
import retrofit.Call

interface WeatherService {
    @GET("2.5/weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appid: String?,
    ) : Call<WeatherResponse>
}