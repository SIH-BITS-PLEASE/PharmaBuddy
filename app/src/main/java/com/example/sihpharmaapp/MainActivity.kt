package com.example.sihpharmaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sihpharmaapp.authentication.SignUpScreen
import com.example.sihpharmaapp.authentication.AuthViewModel
import com.example.sihpharmaapp.authentication.SignInScreen
import com.example.sihpharmaapp.ui.theme.SIHPharmaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SIHPharmaAppTheme {
                val viewModel = viewModel<AuthViewModel>()
                viewModel.setupFirebaseAuth()
                val navController = rememberNavController()
                val startDestination = if (viewModel.isUserSignedIn()) Screens.HomeScreen.route
                else Screens.SignInScreen.route
                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {

                    composable(Screens.SignInScreen.route) {
                        SignInScreen(navController, viewModel)
                    }

                    composable(Screens.SignUpScreen.route) {
                        SignUpScreen(navController, viewModel)
                    }

                    composable(Screens.HomeScreen.route) {
                        HomeScreen(navController,viewModel)
                    }
                }
            }
        }
    }
}