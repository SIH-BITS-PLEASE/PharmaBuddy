package com.example.sihpharmaapp.authentication

sealed class LoginState{
    object Loading : LoginState()
    data class Error(var msg: String? = null) : LoginState()
    object Success : LoginState()

    override fun toString(): String {
        return when(this){
            is Loading ->{
                ""
            }
            is Error ->{
                msg.toString()
            }
            is Success ->{
                ""
            }
        }
    }
}
