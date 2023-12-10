package com.yilmaz.weatherapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarHelper {

    fun snackbar(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
    }

}