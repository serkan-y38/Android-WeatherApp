package com.yilmaz.weatherapp.utils

import android.content.Context
import android.content.Intent

class ShareHelper {

    fun shareWeather(text: String, context: Context): Unit {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, "Share weather"))
    }

    fun shareApp(context: Context): Unit {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Use this app, here is link.")
        context.startActivity(Intent.createChooser(intent, "Share app"))
    }

}