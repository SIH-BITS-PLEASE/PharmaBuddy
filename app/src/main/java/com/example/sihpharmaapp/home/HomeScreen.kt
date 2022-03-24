package com.example.sihpharmaapp.home

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.sihpharmaapp.CustomButtonRect
import com.example.sihpharmaapp.SystemColors
import com.example.sihpharmaapp.authentication.AuthViewModel
import com.example.sihpharmaapp.authentication.CustomTextField
import com.example.sihpharmaapp.data.MedicineDetails
import com.example.sihpharmaapp.data.PharmacyDetails
import com.example.sihpharmaapp.ui.theme.buttonBackgroundColor
import com.example.sihpharmaapp.ui.theme.darkRed
import com.example.sihpharmaapp.ui.theme.dullBlue
import com.example.sihpharmaapp.ui.theme.lightBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.text.DecimalFormat
import java.util.Calendar

var lat: Double? = null
var long: Double? = null

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    sharedPreferences: SharedPreferences
) {
    val calendar = Calendar.getInstance()
    val timeHr = calendar.get(Calendar.HOUR_OF_DAY)
    val currTime = calendar.get(Calendar.MINUTE) + (timeHr * 60)
    homeViewModel.getPharmaciesList()
    SystemColors(
        navigationBarColor = dullBlue,
        systemBarsColor = dullBlue,
        statusBarColor = dullBlue
    )
    val user = authViewModel.getCurrentUser()
    val context = LocalContext.current
    if (homeViewModel.sharedPreferenceState.value) {
        lat = sharedPreferences.getString("latitude", null)!!.toDouble()
        long = sharedPreferences.getString("longitude", null)!!.toDouble()
    }
    val list = homeViewModel.pharmaciesListState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(dullBlue)
    ) {
        var medicineValueState by remember { mutableStateOf("") }
        var quantityValueState by remember { mutableStateOf("") }
        var querySearched by remember { mutableStateOf(false) }
        SearchSection(
            medicineValue = medicineValueState,
            quantityValue = quantityValueState,
            medicineValueChanged = {
                medicineValueState = it
            },
            quantityValueChanged = {
                quantityValueState = it
            },
            searchClicked = {
                querySearched = true
            },
            clearClicked = {
                querySearched = false
                medicineValueState = ""
                quantityValueState = ""
            }
        )
        if (querySearched) {
            LazyColumn {
                list.value?.forEach { pharmacy ->
                    val openTime = getTimeinMins(pharmacy.opening)
                    val closeTime = getTimeinMins(pharmacy.closing)
                    Log.d("Times", "Open -> $openTime, close -> $closeTime, curr-> $currTime")
                    pharmacy.meds?.let { medicines ->
                        medicines.forEach { currMedicine ->
                            try {
                                if (checkStrings(
                                        medicineValueState.lowercase(),
                                        currMedicine.name.lowercase()
                                    ) && quantityValueState.toInt() <= currMedicine.quantity
                                    && currTime >= openTime && currTime <= closeTime
                                ) {
                                    item {
                                        ListItem(
                                            pharmacyDetails = pharmacy,
                                            medicineDetails = currMedicine,
                                            closingIn = closeTime - currTime
                                        ) {
                                            pharmacy.location?.let { location ->
                                                val lat = location[0]
                                                val long = location[1]
                                                val mapIntent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("http://maps.google.com/maps?daddr=$lat,$long")
                                                )
                                                context.startActivity(mapIntent)
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    pharmacyDetails: PharmacyDetails,
    medicineDetails: MedicineDetails,
    closingIn: Int,
    navigateClicked: () -> Unit
) {
    Surface(
        modifier = modifier.padding(10.dp),
        onClick = { /*TODO*/ },
        color = lightBlue,
        shape = RoundedCornerShape(20),
        elevation = 2.dp
    ) {
        Column {
            Row {
                Text(pharmacyDetails.name, modifier.weight(.3f), color = Color.Black)
                Text(pharmacyDetails.address, modifier.weight(.7f))
            }

            Row {
                Text(medicineDetails.name, modifier.weight(.6f))
                Text(medicineDetails.quantity.toString(), modifier.weight(.3f))
            }

            Row {
                Text("Enter distance here!!", modifier.weight(.3f))
                Button(
                    onClick = { navigateClicked() },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                ) {
                    Text("View on map.")
                }
            }
        }
    }
}

private fun getTimeinMins(timeStr: String): Int {
    val times = timeStr.split(":")
    val hrs = Integer.parseInt(times[0])
    return Integer.parseInt(times[1]) + (hrs * 60)
}

private fun checkStrings(pat: String, txt: String): Boolean {
    val M = pat.length
    val N = txt.length
    var found = false

    val lps = IntArray(M)
    var j = 0
    computeLPSArray(pat, M, lps)
    var i = 0
    while (i < N) {
        if (pat[j] == txt[i]) {
            j++
            i++
        }
        if (j == M) {
            found = true
            break
        } else if (i < N && pat[j] != txt[i]) {
            if (j != 0) j = lps[j - 1] else i += 1
        }
    }
    return found
}

fun computeLPSArray(pat: String, M: Int, lps: IntArray) {
    var len = 0
    var i = 1
    lps[0] = 0
    while (i < M) {
        if (pat[i] == pat[len]) {
            len++
            lps[i] = len
            i++
        } else {
            if (len != 0) {
                len = lps[len - 1]
            } else {
                lps[i] = len
                i++
            }
        }
    }
}

@Composable
fun SearchSection(
    modifier: Modifier = Modifier,
    medicineValue: String,
    quantityValue: String,
    medicineValueChanged: (String) -> Unit,
    quantityValueChanged: (String) -> Unit,
    searchClicked: () -> Unit,
    clearClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            label = "Medicine Name",
            placeholder = "Enter the medicine's name",
            value = medicineValue,
            keyboardType = KeyboardType.Text,
            textVisible = true,
            onValueChange = {
                medicineValueChanged(it)
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                modifier = Modifier
                    .weight(.4f)
                    .padding(
                        start = 12.dp,
                        end = 6.dp
                    ),
                label = "Quantity",
                placeholder = "Quantity",
                value = quantityValue,
                keyboardType = KeyboardType.Number,
                textVisible = true,
                onValueChange = {
                    quantityValueChanged(it)
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
            CustomButtonRect(
                modifier
                    .weight(.3f)
                    .padding(end = 6.dp, top = 8.dp),
                text = "Search",
                backgroundColor = buttonBackgroundColor,
                enabled = (medicineValue.isNotEmpty() && quantityValue.isNotEmpty())
            ) {
                searchClicked()
            }
            CustomButtonRect(
                modifier
                    .weight(.3f)
                    .padding(end = 6.dp, top = 8.dp),
                text = "Clear",
                backgroundColor = darkRed,
                enabled = true
            ) {
                clearClicked()
            }
        }
    }
}

fun distanceBetweenPoints(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val Radius = 6371
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + (Math.cos(Math.toRadians(lat1))
        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
        * Math.sin(dLon / 2)))
    val c = 2 * Math.asin(Math.sqrt(a))
    val valueResult = Radius * c
    val km = valueResult / 1
    val newFormat = DecimalFormat("####")
    val kmInDec: Int = Integer.valueOf(newFormat.format(km))
    val meter = valueResult % 1000
    val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
    Log.i(
        "Radius Value", "" + valueResult + "   KM  " + kmInDec
            + " Meter   " + meterInDec
    )
    return Radius * c
}