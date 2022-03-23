package com.example.sihpharmaapp

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.sihpharmaapp.authentication.AuthViewModel
import com.example.sihpharmaapp.home.HomeViewModel
import com.example.sihpharmaapp.navigation.AppNavHost
import com.example.sihpharmaapp.ui.theme.SIHPharmaAppTheme

class MainActivity : ComponentActivity() {
    private var currentLocation: Location? = null
    private lateinit var locationManager: LocationManager
    private var locationByGps: Location? = null
    private var locationByNetwork: Location? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var homeViewModel: HomeViewModel
    private val gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationByGps = location
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private val networkLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationByNetwork = location
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = this.getSharedPreferences(
            "LocationCoordinates", Context.MODE_PRIVATE
        )
        editor = sharedPref.edit()
        getLocation()
        setContent {
            SIHPharmaAppTheme {
                val authViewModel = viewModel<AuthViewModel>()
                homeViewModel = viewModel()
                val navController = rememberNavController()
                val startDestination = if (authViewModel.isUserSignedIn()) Screens.HomeScreen.route
                else Screens.SignInScreen.route
                AppNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel,
                    sharedPreferences = sharedPref
                )
            }
        }
    }

    private fun checkAccuracy() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val lastKnownLocationByGps =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocationByGps?.let {
            locationByGps = lastKnownLocationByGps
        }
        val lastKnownLocationByNetwork =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastKnownLocationByNetwork?.let {
            locationByNetwork = lastKnownLocationByNetwork
        }
        if (locationByGps != null && locationByNetwork != null) {
            if (locationByGps!!.accuracy > locationByNetwork!!.accuracy) {
                currentLocation = locationByGps
                latitude = currentLocation!!.latitude
                longitude = currentLocation!!.longitude
            } else {
                currentLocation = locationByNetwork
                latitude = currentLocation!!.latitude
                longitude = currentLocation!!.longitude
            }
            editor.putString("latitude", latitude.toString())
            editor.putString("longitude", longitude.toString())
            editor.commit()
        }
    }

    private fun getLocation() {
        if (locationPermGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (hasGps) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    gpsLocationListener
                )
            }

            if (hasNetwork) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    networkLocationListener
                )
            }
            checkAccuracy()
        }
    }

    private fun locationPermGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestCode
            )
            false
        } else {
            true
        }
    }

    private val requestCode = 100
}