package com.yilmaz.weatherapp.ui.view_models

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedPreferencesViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    @SuppressLint("CommitPrefEdits")
    fun setTheme(darkTheme: Boolean) = viewModelScope.launch {
        sharedPreferences.edit().putBoolean(THEME, darkTheme).apply()
    }

    fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME, false)
    }

    fun setLastKnownCityName(value: String) {
        sharedPreferences.edit().putString(LAST_KNOWN_CITY, value).apply()
    }

    fun getLastKnownCityName(): String {
        return sharedPreferences.getString(LAST_KNOWN_CITY, "London").toString()
    }

    fun setOnBoardingFinished() = viewModelScope.launch {
        sharedPreferences.edit().putBoolean(ONBOARDING_FINISHED, true).apply()
    }

    fun isOnBoardingFinished(): Boolean {
        return sharedPreferences.getBoolean(ONBOARDING_FINISHED, false)
    }

    companion object {
        const val LAST_KNOWN_CITY = "last_known_city"
        const val ONBOARDING_FINISHED = "onboarding_finished"
        const val THEME = "theme"
    }

}