package com.example.sihpharmaapp.authentication

sealed class LoginState{
    object Loading: LoginState()
    object Error: LoginState()
    object Success: LoginState()
    object Idle: LoginState()
}
