package com.example.sihpharmaapp.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sihpharmaapp.data.PharmacyDetails
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow

class HomeViewModel : ViewModel() {
    private val db by lazy { Firebase.firestore }
    val sharedPreferenceState = mutableStateOf(false)
    val pharmaciesList = flow<List<PharmacyDetails>> {
        val ref = db.collection("Pharma")
        ref.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                emit(task.result as List<PharmacyDetails>)
            } else {

            }
        }
    }
}