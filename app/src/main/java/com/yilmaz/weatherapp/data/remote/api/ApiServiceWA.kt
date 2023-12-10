package com.yilmaz.weatherapp.data.remote.api

import com.yilmaz.weatherapp.data.remote.api.models.weather_api.WeatherApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// ApiServiceWeatherApi
interface ApiServiceWA {

    @GET("forecast.json")
    suspend fun getHourlyWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): Response<WeatherApiModel>

}