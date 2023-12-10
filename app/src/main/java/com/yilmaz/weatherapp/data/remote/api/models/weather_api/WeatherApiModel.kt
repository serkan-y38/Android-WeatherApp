package com.yilmaz.weatherapp.data.remote.api.models.weather_api

data class WeatherApiModel(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)