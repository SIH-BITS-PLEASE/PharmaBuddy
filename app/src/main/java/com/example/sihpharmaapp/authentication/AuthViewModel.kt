package com.example.sihpharmaapp.authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sihpharmaapp.authentication.LoginState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth

    var resetPasswordState = mutableStateOf<LoginState>(LoginState.Idle)

    fun setupFirebaseAuth() {
        auth = Firebase.auth
    }

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun signInUser(
        email: String,
        password: String
    ) = flow {
        try {
            emit(LoginState.Loading)
            auth.signInWithEmailAndPassword(email, password).await()
            if (auth.currentUser != null) {
                emit(LoginState.Success)
            } else {
                emit(LoginState.Error)
            }
        } catch (e: Exception) {
            emit(LoginState.Error)
        }
    }

    fun sendPasswordResetMail(
        email: String
    ) {
        try{
            resetPasswordState.value = LoginState.Loading
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if(task.isSuccessful){
                    resetPasswordState.value = LoginState.Success
                }else{
                    resetPasswordState.value = LoginState.Error
                }
            }
        }catch (e: Exception){
            resetPasswordState.value = LoginState.Error
        }
    }

    suspend fun signUpUser(
        email: String,
        password: String
    ) = flow {
        try {
            emit(LoginState.Loading)
            auth.createUserWithEmailAndPassword(email, password).await()
            if (auth.currentUser != null) {
                emit(LoginState.Success)
            } else {
                emit(LoginState.Error)
            }
        } catch (e: Exception) {
            emit(LoginState.Error)
        }
    }
}