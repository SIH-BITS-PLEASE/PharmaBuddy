package com.example.sihpharmaapp.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sihpharmaapp.data.PharmacyDetails
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val db by lazy { Firebase.firestore }
    val sharedPreferenceState = mutableStateOf(false)
    val progressBarState = mutableStateOf(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _pharmaciesListState: MutableStateFlow<List<PharmacyDetails>?> =
        MutableStateFlow(null)
    val pharmaciesListState: StateFlow<List<PharmacyDetails>?> get() = _pharmaciesListState

    fun getPharmaciesList() {
        val ref = db.collection("Pharma")
        ref.addSnapshotListener { snapshot, error ->
            error?.let {
                progressBarState.value = false
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

            // for (dc in snapshot!!.documentChanges) {
            //     if (dc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
            //         val pharmacy = dc.document.data.toString()
            //         Log.d("DataTest", pharmacy)
            //         val p = Klaxon().parse<PharmacyDetails> (pharmacy)
            //         viewModelScope.launch {
            //             p?.let {
            //                 emit(p)
            //             }
            //         }
            //         // val address: String = dc.document.data.getValue("address") as String
            //         // Log.d("DataTest", "address -> $address")
            //         // val location: List<String> = dc.document.data.getValue("location") as List<String>
            //         // val meds: List<MedicineDetails> = dc.document.data.getValue("meds") as List<MedicineDetails>
            //         // val name: String = dc.document.data.getValue("name") as String
            //         // val id: String = dc.document.data.getValue("id") as String
            //         // Log.d("DataTest", "address -> $address, name -> $name, id -> $id")
            //         // viewModelScope.launch {
            //         //     emit(PharmacyDetails(address,location,meds,name,id))
            //         // }
            //     } else {
            //         Log.d("DataTest", "Else block -> ${dc.document.data}")
            //     }
            // }
        }
    }
}