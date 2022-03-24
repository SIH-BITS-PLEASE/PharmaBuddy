package com.example.sihpharmaapp.data

data class PharmacyDetails(
    val address: String = "",
    val closing: String = "",
    val id: String = "",
    val location: List<Double>? = null,
    val meds: List<MedicineDetails>? = null,
    val name: String = "",
    val opening: String = ""
)
