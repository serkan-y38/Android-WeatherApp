package com.yilmaz.weatherapp.data.remote.api.models.open_w_map

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)