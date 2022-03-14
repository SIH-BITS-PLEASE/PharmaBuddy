package com.example.sihpharmaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.sihpharmaapp.authentication.AuthViewModel
import com.example.sihpharmaapp.ui.theme.homeBackground
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(homeBackground)
    systemUiController.setSystemBarsColor(homeBackground)
    systemUiController.setStatusBarColor(homeBackground)
    val user = viewModel.getCurrentUser()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(homeBackground)
    ) {
        user?.let {
            Text(text = "Email Id -> ${user.email}")
        }
    }
}