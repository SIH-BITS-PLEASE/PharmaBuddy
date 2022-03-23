package com.example.sihpharmaapp.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sihpharmaapp.data.PharmacyDetails
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow

class HomeViewModel : ViewModel() {
    private val db by lazy { Firebase.firestore }
    val sharedPreferenceState = mutableStateOf(false)
    val progressBarState = mutableStateOf(true)
    fun getPharmaciesList() = flow<PharmacyDetails> {
        Log.d("DataTest", "HERE2!!")
        val ref = db.collection("Pharma")
        ref.addSnapshotListener { snapshot, error ->
            Log.d("DataTest", "HERE!!")
            if (error != null) {
                // error
                Log.d("DataTest", "Error")
                progressBarState.value = false
                return@addSnapshotListener
            }
            for (dc in snapshot!!.documentChanges) {
                if (dc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                    Log.d("DataTest", dc.document.data.toString())
                    dc.document.data.map {
                        it.value to PharmacyDetails::class.java
                    }
                    // val address: String = dc.document.data.getValue("address") as String
                    // Log.d("DataTest", "address -> $address")
                    // val location: List<String> = dc.document.data.getValue("location") as List<String>
                    // val meds: List<MedicineDetails> = dc.document.data.getValue("meds") as List<MedicineDetails>
                    // val name: String = dc.document.data.getValue("name") as String
                    // val id: String = dc.document.data.getValue("id") as String
                    // Log.d("DataTest", "address -> $address, name -> $name, id -> $id")
                    // viewModelScope.launch {
                    //     emit(PharmacyDetails(address,location,meds,name,id))
                    // }
                } else {
                    Log.d("DataTest", "Else block -> ${dc.document.data}")
                }
            }
        }
    }
}