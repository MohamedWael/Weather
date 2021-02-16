package com.github.mohamedwael.weather.modules.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mohamedwael.weather.R
import com.github.mohamedwael.weather.databinding.WeatherFragmentBinding
import com.github.mohamedwael.weather.setup.hideKeyboard
import com.github.mohamedwael.weather.setup.needPermissions
import java.util.*

class WeatherFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = WeatherFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setIsPermissionGranted(
            needPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, requestCode = PERMISSIONS_REQUEST_CODE
            ) != true
        )

        viewModel.permissionsGranted.observe(viewLifecycleOwner) { isGranted ->
            context?.also {
                if (isGranted) {
                    viewModel.geoCoder = Geocoder(it, Locale.getDefault())
                    getLocation(it)
                } else {
                    Toast.makeText(it, R.string.location_error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.hideKeyboard.observe(viewLifecycleOwner){
            if (it){
                activity?.hideKeyboard()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(context: Context) {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        viewModel.onLocationUpdate(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(requestCode, grantResults)
    }

}
