package com.github.mohamedwael.weather.setup

import android.app.Application
import com.github.mohamedwael.weather.BuildConfig
import com.github.mohamedwael.weather.modules.weather.repo.WeatherRepo
import com.github.mohamedwael.weather.modules.weather.repo.WeatherService
import com.github.mohamedwael.weather.modules.weather.WeatherViewModelFactory
import com.github.mohamedwael.weather.setup.network.ApiClient
import com.github.mohamedwael.weather.setup.network.RetrofitConfig
import com.github.mohamedwael.weather.setup.network.getApiInterceptor

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = RetrofitConfig.Builder()
            .url(END_POINT)
            .addInterceptor(getApiInterceptor())
            .enableLogger(BuildConfig.DEBUG)
            .build()
        val apiService = config.createClient(ApiClient::class.java)

        WeatherViewModelFactory.inject(
            WeatherRepo(
                WeatherService(apiService)
            )
        )
    }
}