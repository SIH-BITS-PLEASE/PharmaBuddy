package com.example.sihpharmaapp.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sihpharmaapp.data.PharmacyDetails
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val db by lazy { Firebase.firestore }
    val sharedPreferenceState = mutableStateOf(false)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _pharmaciesListState: MutableStateFlow<List<PharmacyDetails>?> =
        MutableStateFlow(null)
    val pharmaciesListState: StateFlow<List<PharmacyDetails>?> get() = _pharmaciesListState
    private lateinit var listener: ListenerRegistration

    fun getPharmaciesList() {
        val ref = db.collection("Pharma")
        listener = ref.addSnapshotListener { snapshot, error ->
            Log.d("requestTest", "snapshot listeners!")
            error?.let {
                return@addSnapshotListener
            }
            snapshot?.let { pharmacies ->
                val list = mutableListOf<PharmacyDetails>()
                for (pharmacy in pharmacies.documentChanges) {
                    if (pharmacy.type == DocumentChange.Type.ADDED) {
                        val curr = pharmacy.document.toObject<PharmacyDetails>()
                        list.add(curr)
                        Log.d("DataTest", "address -> ${curr.address}")
                    }
                }
                _pharmaciesListState.value = list
            }
        }
    }

    override fun onCleared() {
        listener.remove()
        super.onCleared()
    }
}