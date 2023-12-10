package com.yilmaz.weatherapp.repositories

import com.yilmaz.weatherapp.data.remote.api.ApiServiceWA
import javax.inject.Inject

class WeatherApiRepository @Inject constructor(private val apiServiceWA: ApiServiceWA) {

    suspend fun getHourlyWeather(key: String, city: String) =
        apiServiceWA.getHourlyWeather(key, city)

}