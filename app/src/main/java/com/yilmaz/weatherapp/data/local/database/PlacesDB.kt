package com.yilmaz.weatherapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [PlacesModel::class], version = 1, exportSchema = false)
abstract class PlacesDB : RoomDatabase() {
    abstract fun placesDAO(): PlacesDAO
}