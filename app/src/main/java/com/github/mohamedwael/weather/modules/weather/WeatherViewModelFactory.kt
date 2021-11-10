package com.github.mohamedwael.weather.modules.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.mohamedwael.weather.modules.weather.repo.WeatherRepo

object WeatherViewModelFactory : ViewModelProvider.Factory {

    private lateinit var repo: WeatherRepo
    fun inject(weatherRepo: WeatherRepo) {
        repo = weatherRepo
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (WeatherViewModel::class.java.isAssignableFrom(modelClass)) {
            return WeatherViewModel(repo) as T
        } else {
            throw IllegalStateException("ViewModel must be MoviesViewModel")
        }
    }
}