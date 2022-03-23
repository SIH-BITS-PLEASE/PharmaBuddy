package com.example.sihpharmaapp.data

data class PharmacyDetails(
    val address: String,
    val location: List<String>,
    val meds: List<MedicineDetails>,
    val name: String,
    val id: String
)
