package com.example.sihpharmaapp.home

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sihpharmaapp.CustomButtonRect
import com.example.sihpharmaapp.R
import com.example.sihpharmaapp.Screens
import com.example.sihpharmaapp.SystemColors
import com.example.sihpharmaapp.authentication.AuthViewModel
import com.example.sihpharmaapp.authentication.CustomTextField
import com.example.sihpharmaapp.data.MedicineDetails
import com.example.sihpharmaapp.data.PharmacyDetails
import com.example.sihpharmaapp.ui.theme.buttonBackgroundColor
import com.example.sihpharmaapp.ui.theme.darkGray
import com.example.sihpharmaapp.ui.theme.darkRed
import com.example.sihpharmaapp.ui.theme.dullBlue
import com.example.sihpharmaapp.ui.theme.dullGreen
import com.example.sihpharmaapp.ui.theme.dullWhite
import com.example.sihpharmaapp.ui.theme.homeBackground
import com.example.sihpharmaapp.ui.theme.lightBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.text.DecimalFormat
import java.util.Calendar

var lat: Double? = null
var long: Double? = null

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    sharedPreferences: SharedPreferences
) {
    val calendar = Calendar.getInstance()
    val timeHr = calendar.get(Calendar.HOUR_OF_DAY)
    val currTime = calendar.get(Calendar.MINUTE) + (timeHr * 60)
    homeViewModel.getPharmaciesList()
    var alertState by remember {
        mutableStateOf(false)
    }
    SystemColors(
        navigationBarColor = dullBlue,
        systemBarsColor = dullBlue,
        statusBarColor = dullBlue
    )
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(50.dp)
                    .clickable {
                        alertState = true
                    },
                shape = RoundedCornerShape(4.dp),
                elevation = 4.dp,
                color = darkRed
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.exit),
                        contentDescription = "log out",
                        tint = dullWhite
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Out",
                        color = dullWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        if (alertState) {
            LogOutAlert(
                onCancelClicked = { alertState = false },
                onLogOutClicked = {
                    authViewModel.auth.signOut()
                    navController.navigate(Screens.SignInScreen.route) {
                        popUpTo(Screens.HomeScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun LogOutAlert(
    onCancelClicked: () -> Unit,
    onLogOutClicked: () -> Unit
) {
    AlertDialog(
        backgroundColor = homeBackground,
        onDismissRequest = {},
        title = {
            Text(
                text = "Are you sure you want to log out?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomButtonRect(
                    text = "Log Out",
                    backgroundColor = darkRed,
                    enabled = true
                ) {
                    onLogOutClicked()
                }

                Spacer(modifier = Modifier.width(12.dp))

                CustomButtonRect(
                    text = "Cancel",
                    backgroundColor = dullGreen,
                    enabled = true
                ) {
                    onCancelClicked()
                }
            }
        },
    )
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
        modifier = modifier
            .padding(6.dp)
            .padding(top = 10.dp, bottom = 10.dp),
        onClick = {},
        color = lightBlue,
        shape = RoundedCornerShape(4.dp),
        elevation = 2.dp
    ) {
        val pLat = pharmacyDetails.location?.get(0)
        val pLong = pharmacyDetails.location?.get(1)
        Column {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextWithImage(
                    painter = painterResource(id = R.drawable.building),
                    text = pharmacyDetails.name
                )
                TextWithImage(
                    painter = painterResource(id = R.drawable.building),
                    text = pharmacyDetails.address
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextWithImage(
                    painter = painterResource(id = R.drawable.medicine_name),
                    text = medicineDetails.name
                )
                TextWithImage(
                    painter = painterResource(id = R.drawable.number),
                    text = medicineDetails.quantity.toString()
                )
                // navigation button
                CustomButtonRect(text = "Navigate", backgroundColor = dullGreen, enabled = true) {
                    navigateClicked()
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (pLat != null && pLong != null && lat != null && long != null) {
                    val dist = distanceBetweenPoints(pLat, pLong, lat!!, long!!)
                    TextWithImage(
                        painter = painterResource(id = R.drawable.distance),
                        text = dist.toString()
                    )
                }
                TextWithImage(
                    painter = painterResource(id = R.drawable.timer),
                    text = "$closingIn mins"
                )
                TextWithImage(
                    painter = painterResource(id = R.drawable.price),
                    text = medicineDetails.price.toString()
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun TextWithImage(
    painter: Painter,
    text: String
) {
    Row {
        Icon(painter = painter, contentDescription = text)
        Spacer(modifier = Modifier.width(2.dp))
        Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis, color = darkGray)
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

fun distanceBetweenPoints(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
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
    return Integer.valueOf(newFormat.format(km))
}