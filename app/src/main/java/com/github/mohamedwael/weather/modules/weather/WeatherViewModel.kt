package com.github.mohamedwael.weather.modules.weather

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.*
import com.github.mohamedwael.weather.modules.weather.repo.WeatherRepo
import com.github.mohamedwael.weather.setup.getCurrentDate
import com.github.mohamedwael.weather.setup.network.Status

const val PERMISSIONS_REQUEST_CODE = 105

class WeatherViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {

    private val _hideKeyboard = MutableLiveData<Boolean>()
    val hideKeyboard: LiveData<Boolean>
        get() = _hideKeyboard

    private val _permissionsGranted = MutableLiveData<Boolean>()
    val permissionsGranted: LiveData<Boolean>
        get() = _permissionsGranted

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?>
        get() = _errorMsg

    var geoCoder: Geocoder? = null
    private val location = MutableLiveData<Location>()

    val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            _hideKeyboard.value = true
            search(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            //not needed
            return true
        }
    }

    private val _cityName = MediatorLiveData<String>().apply {
        addSource(location) { location ->
            _submitQuery.value = getCity(location)?.let {
                value = it
                true
            } ?: false
        }
    }

    val cityName: LiveData<String>
        get() = _cityName

    private val _submitQuery = MutableLiveData<Boolean>()
    val submitQuery: LiveData<Boolean>
        get() = _submitQuery

    private val _isLoading = MediatorLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    private val _date = MutableLiveData(getCurrentDate())
    val date: LiveData<String>
        get() = _date

    private val _temperature = MutableLiveData<String>()
    val temperature: LiveData<String>
        get() = _temperature


    private val _minMaxTemperature = MutableLiveData<String>()
    val minMaxTemperature: LiveData<String>
        get() = _minMaxTemperature

    private val _feelsLike = MutableLiveData<String>()
    val feelsLike: LiveData<String>
        get() = _feelsLike

    private val _icon = MutableLiveData<String?>()
    val icon: LiveData<String?>
        get() = _icon

    private val _weatherDescription = MutableLiveData<String?>()
    val weatherDescription: LiveData<String?>
        get() = _weatherDescription


    fun setIsPermissionGranted(isGranted: Boolean) {
        _permissionsGranted.value = isGranted
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            _permissionsGranted.value = (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    private fun getCity(location: Location): String? = geoCoder?.getFromLocation(
        location.latitude,
        location.longitude,
        1
    )?.get(0)?.subAdminArea


    fun onLocationUpdate(location: Location?) {
        location?.also {
            this.location.value = it
        }
    }

    fun search(query: String?) {
        query?.also {
            if (it.isNotEmpty()) {
                val resource = weatherRepo.getWeatherByCity(query)
                resource.observeForever { weatherResponse->
                    when (weatherResponse?.status) {
                        Status.LOADING -> {
                            _isLoading.value = true
                        }
                        Status.SUCCESS -> {
                            _isLoading.value = false
                            weatherResponse.data?.main?.also { main ->
                                _temperature.value = "${main.temp}°C"
                                _minMaxTemperature.value = "${main.tempMax},${main.tempMin}"
                                _feelsLike.value = "${main.feelsLike?:""}"
                                _icon.value = weatherResponse?.data?.weather?.get(0)?.icon
                                _weatherDescription.value = weatherResponse?.data?.weather?.get(0)?.description
                            }
                        }
                        Status.ERROR -> {
                            _isLoading.value = false
                            _errorMsg.value = weatherResponse.message
                        }
                    }
                }
                _isLoading.addSource(resource){weatherResponse->

                }
            }
        }
    }
}
