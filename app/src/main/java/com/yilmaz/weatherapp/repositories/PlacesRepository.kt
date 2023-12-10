package com.yilmaz.weatherapp.repositories

import com.yilmaz.weatherapp.data.local.database.PlacesDAO
import com.yilmaz.weatherapp.data.local.database.PlacesModel
import javax.inject.Inject

class PlacesRepository @Inject constructor(private val dao: PlacesDAO) {

    suspend fun insertPlace(model: PlacesModel) = dao.insertPlace(model)

    suspend fun deletePlace(model: PlacesModel) = dao.deletePlace(model)

    fun getAllPlaces() = dao.getAllPlaces()

}