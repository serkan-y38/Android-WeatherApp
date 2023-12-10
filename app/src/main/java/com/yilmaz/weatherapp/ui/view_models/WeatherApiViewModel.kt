package com.yilmaz.weatherapp.ui.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yilmaz.weatherapp.data.remote.api.models.weather_api.WeatherApiModel
import com.yilmaz.weatherapp.repositories.WeatherApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherApiViewModel @Inject constructor(
    private val repository: WeatherApiRepository,
) : ViewModel() {
    private val _responseHourly = MutableLiveData<WeatherApiModel>()
    val responseHourly: LiveData<WeatherApiModel> get() = _responseHourly

    fun getHourlyResponse(key: String, city: String) = getHourlyWeather(key, city)

    private fun getHourlyWeather(key: String, city: String) = viewModelScope.launch {
        repository.getHourlyWeather(key, city).let { response ->
            if (response.isSuccessful)
                _responseHourly.postValue(response.body())
            else
                Log.e("response", "error: ${response.code()}")
        }

    }

}