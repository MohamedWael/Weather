package com.github.mohamedwael.weather.modules.weather

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val PERMISSIONS_REQUEST_CODE = 105

class WeatherViewModel : ViewModel() {

    private val _hideKeyboard = MutableLiveData<Boolean>()
    val hideKeyboard: LiveData<Boolean>
        get() = _hideKeyboard

    private val _permissionsGranted = MutableLiveData<Boolean>()
    val permissionsGranted: LiveData<Boolean>
        get() = _permissionsGranted

    var geoCoder: Geocoder? = null
    private val location = MutableLiveData<Location>()

    val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            _hideKeyboard.value = true
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            search(newText)
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
    )?.get(0)?.getAddressLine(0)


    fun onLocationUpdate(location: Location?) {
        location?.also {
            this.location.value = it
        }
    }

    fun search(query: String?) {

    }

}
