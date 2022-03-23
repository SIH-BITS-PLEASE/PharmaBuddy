package com.example.sihpharmaapp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.sihpharmaapp.SystemColors
import com.example.sihpharmaapp.ui.theme.homeBackground

@Composable
fun PermissionsNotGrantedScreen(
    homeViewModel: HomeViewModel
) {
    SystemColors(
        navigationBarColor = homeBackground,
        systemBarsColor = homeBackground,
        statusBarColor = homeBackground
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(homeBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Permissions declined permanently, please go to the settings and enable them to continue.")
    }
}