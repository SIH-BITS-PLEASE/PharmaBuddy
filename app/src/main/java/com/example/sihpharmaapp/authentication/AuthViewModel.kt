package com.example.sihpharmaapp.authentication

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth

    private val _resetPasswordState : MutableStateFlow<LoginState?> = MutableStateFlow(null)
    val resetPasswordState : StateFlow<LoginState?> get() = _resetPasswordState
    private val _signInState : MutableStateFlow<LoginState?> = MutableStateFlow(null)
    val signInState : StateFlow<LoginState?> get() = _signInState
    private val _signUpState : MutableStateFlow<LoginState?> = MutableStateFlow(null)
    val signUpState : StateFlow<LoginState?> get() = _signUpState

    fun setupFirebaseAuth() {
        auth = Firebase.auth
    }

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun signInUser(
        email: String,
        password: String
    ) {
        try {
            _signInState.value = LoginState.Loading
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                if(task.isSuccessful){
                    _signInState.value = LoginState.Success
                }else{
                    _signInState.value = LoginState.Error(task.exception.toString())
                }
            }
        } catch (e: Exception) {
            _signInState.value = LoginState.Error()
        }
    }

    fun sendPasswordResetMail(
        email: String
    ) {
        try{
            _resetPasswordState.value = LoginState.Loading
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if(task.isSuccessful){
                    _resetPasswordState.value = LoginState.Success
                }else{
                    _resetPasswordState.value = LoginState.Error(task.exception.toString())
                }
            }
        }catch (e: Exception){
            _resetPasswordState.value = LoginState.Error()
        }
    }

    fun signUpUser(
        email: String,
        password: String
    ) {
        try {
            _signUpState.value = LoginState.Loading
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task->
                if(task.isSuccessful){
                    _signUpState.value = LoginState.Success
                }else{
                    _signUpState.value = LoginState.Error(task.exception.toString())
                }
            }
        } catch (e: Exception) {
            _signUpState.value = LoginState.Error(e.message.toString())
        }
    }
}