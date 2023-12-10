package com.yilmaz.weatherapp.data.remote.api.models.weather_api

data class Forecastday(
    val astro: Astro,
    val date: String,
    val date_epoch: Int,
    val day: Day,
    val hour: List<Hour>
)