package com.yilmaz.weatherapp.repositories

import com.yilmaz.weatherapp.data.remote.api.ApiServiceOW
import javax.inject.Inject

class OpenWeatherRepository @Inject constructor(private val apiServiceOW: ApiServiceOW) {

    suspend fun getDailyWeather(city: String, key: String) = apiServiceOW.getDailyWeather(city, key)

}