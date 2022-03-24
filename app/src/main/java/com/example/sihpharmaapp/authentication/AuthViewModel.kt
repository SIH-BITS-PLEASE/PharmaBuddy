package com.example.sihpharmaapp.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sihpharmaapp.data.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }

    private val _resetPasswordState: MutableStateFlow<ProgressState?> = MutableStateFlow(null)
    val resetPasswordState: StateFlow<ProgressState?> get() = _resetPasswordState

    private val _signInState: MutableStateFlow<ProgressState?> = MutableStateFlow(null)
    val signInState: StateFlow<ProgressState?> get() = _signInState

    private val _signUpState: MutableStateFlow<ProgressState?> = MutableStateFlow(null)
    val signUpState: StateFlow<ProgressState?> get() = _signUpState

    private val _saveUserState: MutableStateFlow<ProgressState?> = MutableStateFlow(null)
    val saveUserState: StateFlow<ProgressState?> get() = _saveUserState

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun saveUserToDB(
        user: User
    ) {
        try {
            _saveUserState.value = ProgressState.Loading
            val userRef = db.collection("users").document(user.userId)
            userRef.set(user).addOnCompleteListener { task ->
                Log.d("requestTest", "on Complete Listeners!!")
                if (task.isSuccessful) {
                    _saveUserState.value = ProgressState.Success
                } else {
                    _saveUserState.value = ProgressState.Error(task.exception.toString())
                }
            }
        } catch (e: Exception) {
            _saveUserState.value = ProgressState.Error(e.message.toString())
        }
    }

    fun signInUser(
        email: String,
        password: String
    ) {
        try {
            _signInState.value = ProgressState.Loading
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                Log.d("requestTest", "on Complete Listeners!!")
                if (task.isSuccessful) {
                    _signInState.value = ProgressState.Success
                } else {
                    _signInState.value = ProgressState.Error(task.exception.toString())
                }
            }
        } catch (e: Exception) {
            _signInState.value = ProgressState.Error(e.message.toString())
        }
    }

    fun sendPasswordResetMail(
        email: String
    ) {
        try {
            _resetPasswordState.value = ProgressState.Loading
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                Log.d("requestTest", "on Complete Listeners!!")
                if (task.isSuccessful) {
                    _resetPasswordState.value = ProgressState.Success
                } else {
                    _resetPasswordState.value = ProgressState.Error(task.exception.toString())
                }
            }
        } catch (e: Exception) {
            _resetPasswordState.value = ProgressState.Error(e.message.toString())
        }
    }

    fun signUpUser(
        email: String,
        password: String
    ) {
        try {
            _signUpState.value = ProgressState.Loading
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                Log.d("requestTest", "on Complete Listeners!!")
                if (task.isSuccessful) {
                    _signUpState.value = ProgressState.Success
                } else {
                    _signUpState.value = ProgressState.Error(task.exception.toString())
                }
            }
        } catch (e: Exception) {
            _signUpState.value = ProgressState.Error(e.message.toString())
        }
    }
}