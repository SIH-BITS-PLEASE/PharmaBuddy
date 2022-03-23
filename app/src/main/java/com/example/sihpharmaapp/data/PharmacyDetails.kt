package com.example.sihpharmaapp.data

data class PharmacyDetails(
    val address: String = "",
    val location: List<*>? = null,
    val meds: List<MedicineDetails>? = null,
    val name: String = "",
    val id: String = ""
)
