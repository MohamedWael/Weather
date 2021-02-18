package com.github.mohamedwael.weather.modules.weather.repo

import androidx.lifecycle.liveData
import com.github.mohamedwael.weather.setup.network.Resource
import kotlinx.coroutines.Dispatchers

class WeatherRepo(private val weatherService: WeatherService) {


    fun getWeatherByCity(query: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(data = weatherService.getWeatherByCity(query)))
        } catch (exception: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = exception.message ?: "Error Occurred!"
                )
            )
        }
    }

}