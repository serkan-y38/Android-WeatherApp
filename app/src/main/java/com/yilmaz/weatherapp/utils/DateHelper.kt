package com.yilmaz.weatherapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    @SuppressLint("SimpleDateFormat")
    fun format(date: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val output = SimpleDateFormat("hh:mm aa")
        var time = ""

        try {
            val d: Date = input.parse(date)!!
            time = output.format(d).toString()

        } catch (e: Exception) {
            e.printStackTrace()

        }
        return time
    }

}