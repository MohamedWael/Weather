package com.github.mohamedwael.weather.modules.weather.repo

import com.github.mohamedwael.weather.setup.network.ApiClient
import java.util.*

class WeatherService(private val restClient: ApiClient) {

    suspend fun getWeatherByCity(city: String) = restClient.getWeatherByCity(
        mapOf(
            Pair("q", city),
            Pair("units", "metric"),
            Pair("lang", Locale.getDefault().language)
        )
    )

}