package com.yilmaz.weatherapp.data.remote.api

import com.yilmaz.weatherapp.data.remote.api.models.open_w_map.OpenWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// ApiServiceOpenWeather
interface ApiServiceOW {

    @GET("forecast")
    suspend fun getDailyWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Response<OpenWeatherModel>

}