package com.github.mohamedwael.weather.setup.network

import com.github.mohamedwael.weather.modules.weather.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiClient {

    @GET("weather")
    suspend fun getWeatherByCity(@QueryMap queries: Map<String, String>): WeatherResponse
}