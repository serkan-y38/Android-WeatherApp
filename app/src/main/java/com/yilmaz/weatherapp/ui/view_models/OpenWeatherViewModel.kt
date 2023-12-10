package com.yilmaz.weatherapp.ui.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yilmaz.weatherapp.data.remote.api.models.open_w_map.OpenWeatherModel
import com.yilmaz.weatherapp.repositories.OpenWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenWeatherViewModel @Inject constructor(
    private val repository: OpenWeatherRepository,
) : ViewModel() {

    private val _responseDaily = MutableLiveData<OpenWeatherModel>()
    val responseDaily: LiveData<OpenWeatherModel> get() = _responseDaily

    fun getDailyResponse(city: String, key: String) = getDailyWeather(city, key)

    private fun getDailyWeather(city: String, key: String) = viewModelScope.launch {
        repository.getDailyWeather(city, key).let { response ->
            if (response.isSuccessful)
                _responseDaily.postValue(response.body())
            else
                Log.e("response", "error: ${response.code()}")
        }

    }

}