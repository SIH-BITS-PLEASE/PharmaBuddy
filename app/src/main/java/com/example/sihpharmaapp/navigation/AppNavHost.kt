package com.example.sihpharmaapp.navigation

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sihpharmaapp.Screens
import com.example.sihpharmaapp.authentication.AuthViewModel
import com.example.sihpharmaapp.authentication.SignInScreen
import com.example.sihpharmaapp.authentication.SignUpScreen
import com.example.sihpharmaapp.home.HomeScreen
import com.example.sihpharmaapp.home.HomeViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    sharedPreferences: SharedPreferences
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screens.SignInScreen.route) {
            SignInScreen(navController, authViewModel)
        }

        composable(Screens.SignUpScreen.route) {
            SignUpScreen(navController, authViewModel)
        }

        composable(Screens.HomeScreen.route) {
            HomeScreen(navController, authViewModel, homeViewModel, sharedPreferences)
        }
    }
}