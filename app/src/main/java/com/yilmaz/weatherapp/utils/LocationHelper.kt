package com.yilmaz.weatherapp.utils

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import java.util.Locale


class LocationHelper {

    fun isLocationEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationEnabled: Boolean
        var gpsEnabled = false
        var networkEnabled = false

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        locationEnabled = gpsEnabled && networkEnabled
        return locationEnabled
    }

    fun getCityName(lat: Double, long: Double, context: Context): String {
        var cityName = ""
        try {
            val geoCoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION") val address = geoCoder.getFromLocation(lat, long, 3)

            if (!address.isNullOrEmpty())
                cityName = address[0].adminArea

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return cityName
    }

}