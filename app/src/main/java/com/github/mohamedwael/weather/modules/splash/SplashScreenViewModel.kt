package com.github.mohamedwael.weather.modules.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val SPLASH_TIMER = 3000L

class SplashScreenViewModel : ViewModel() {
    private val _startSplashCounter = MutableLiveData<Boolean>()
    val startSplashCounter: LiveData<Boolean>
        get() = _startSplashCounter

    init {
        viewModelScope.launch {
            startSplashCounter()
        }
    }

   private suspend fun startSplashCounter() {
        delay(SPLASH_TIMER)
       _startSplashCounter.value = true
    }
}