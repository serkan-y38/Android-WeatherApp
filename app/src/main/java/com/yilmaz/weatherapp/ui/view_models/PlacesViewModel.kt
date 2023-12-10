package com.yilmaz.weatherapp.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yilmaz.weatherapp.data.local.database.PlacesModel
import com.yilmaz.weatherapp.repositories.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(private val repo: PlacesRepository) : ViewModel() {

    fun insertPlace(model: PlacesModel) = viewModelScope.launch {
        repo.insertPlace(model)
    }

    fun deletePlace(model: PlacesModel) = viewModelScope.launch {
        repo.deletePlace(model)
    }

    val getAllPlaces = repo.getAllPlaces().asLiveData()

}