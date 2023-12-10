package com.yilmaz.weatherapp.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(placesModel: PlacesModel)

    @Delete
    suspend fun deletePlace(model: PlacesModel)

    @Query("SELECT * FROM  saved_places ORDER BY id ASC")
    fun getAllPlaces(): Flow<List<PlacesModel>>

}