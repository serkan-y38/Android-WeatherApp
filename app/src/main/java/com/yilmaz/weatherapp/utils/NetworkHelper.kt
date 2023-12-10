package com.yilmaz.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log


class NetworkHelper {

    @Suppress("DEPRECATION")
    fun isConnected(context: Context): Boolean {
        var connected = false
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected

        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message.toString())

        }
        return connected
    }

}