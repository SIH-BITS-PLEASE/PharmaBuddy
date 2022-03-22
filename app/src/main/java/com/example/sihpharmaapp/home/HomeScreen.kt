package com.example.sihpharmaapp.home

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.sihpharmaapp.SystemColors
import com.example.sihpharmaapp.authentication.AuthViewModel
import com.example.sihpharmaapp.ui.theme.homeBackground
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    sharedPreferences: SharedPreferences
) {
    SystemColors(
        navigationBarColor = homeBackground,
        systemBarsColor = homeBackground,
        statusBarColor = homeBackground
    )
    val user = authViewModel.getCurrentUser()
    val context = LocalContext.current
    SystemColors(
        navigationBarColor = homeBackground,
        systemBarsColor = homeBackground,
        statusBarColor = homeBackground
    )
    val lat = sharedPreferences.getString("latitude", null)
    val long = sharedPreferences.getString("longitude", null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(homeBackground)
    ) {
        Text(text = " Location, lat-> $lat, long -> $long")
    }
}