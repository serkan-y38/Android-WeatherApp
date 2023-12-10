package com.yilmaz.weatherapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.yilmaz.weatherapp.data.local.database.PlacesDB
import com.yilmaz.weatherapp.data.remote.api.ApiServiceOW
import com.yilmaz.weatherapp.data.remote.api.ApiServiceWA
import com.yilmaz.weatherapp.utils.Constants.APP_SHARED_DATABASE_NAME
import com.yilmaz.weatherapp.utils.Constants.APP_SHARED_PREFERENCES_NAME
import com.yilmaz.weatherapp.utils.Constants.OPEN_WEATHER_MAP_BASE_URL
import com.yilmaz.weatherapp.utils.Constants.WEATHER_API_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit1Instance(): ApiServiceWA =
        Retrofit.Builder()
            .baseUrl(WEATHER_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceWA::class.java)

    @Provides
    @Singleton
    fun provideRetrofit2Instance(): ApiServiceOW =
        Retrofit.Builder()
            .baseUrl(OPEN_WEATHER_MAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceOW::class.java)

    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, PlacesDB::class.java,
        APP_SHARED_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(
        db: PlacesDB
    ) = db.placesDAO()

    @Singleton
    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            APP_SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

}