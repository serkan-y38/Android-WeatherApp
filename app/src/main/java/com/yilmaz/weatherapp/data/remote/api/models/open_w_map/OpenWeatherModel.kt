package com.yilmaz.weatherapp.data.remote.api.models.open_w_map


data class OpenWeatherModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<item>,
    val message: Int
)