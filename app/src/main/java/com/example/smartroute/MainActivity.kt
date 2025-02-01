package com.example.smartroute

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import kotlinx.parcelize.Parcelize
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartroute.ui.theme.SmartRouteTheme
//import com.google.type.LatLng
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartRouteTheme {
                Scaffold { innerPadding ->
                    MainScreen(innerPadding)
                }
            }
        }
    }
}

//@Preview
@Composable
fun MainScreen(innerpadding: PaddingValues){

    val context = LocalContext.current
    var fileUri1 by remember { mutableStateOf<Uri?>(null) }
    var fileUri2 by remember { mutableStateOf<Uri?>(null) }
    var fileUri3 by remember { mutableStateOf<Uri?>(null) }
    var fileName1 by remember { mutableStateOf("No file selected") }
    var fileName2 by remember { mutableStateOf("No file selected") }
    var fileName3 by remember { mutableStateOf("No file selected") }
//    var trips by remember { mutableStateOf(listOf<Trip>()) }
//    var trips by remember { mutableStateOf<List<Trip>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val filePickerLauncher1 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                fileUri1 = it
                fileName1 = getFileName(it)
                // Further file processing logic goes here
            }
        }
    )

    val filePickerLauncher2 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                fileUri2 = it
                fileName2 = getFileName(it)
                // Further file processing logic goes here
            }
        }
    )

//    val filePickerLauncher3 = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.OpenDocument(),
//        onResult = { uri ->
//            uri?.let {
//                fileUri3 = it
//                fileName3 = getFileName(it)
//                // Further file processing logic goes here
//            }
//        }
//    )

    val trips = listOf(
        Trip(
            "1",
            listOf(
                Shipment(1, LatLng(19.076, 72.877), "10:00 AM"),
                Shipment(2, LatLng(28.704, 77.102), "12:30 PM")
            ),
            50.0, 1.5, "Truck", 80.0, 60.0, 75.0
        ),
        Trip(
            "2",
            listOf(
                Shipment(3, LatLng(28.704, 77.102), "2:00 PM"),
                Shipment(4, LatLng(19.076, 72.877), "4:30 PM")
            ),
            30.0, 1.0, "Van", 70.0, 50.0, 65.0
        )
    )

    Column(
        modifier = Modifier.padding(innerpadding)
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Upload CSV/Excel Files",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FileUploadButton(
                        text = "Select Shipments File",
                        fileName = fileName1,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        filePickerLauncher1.launch(arrayOf("*/*"))
                    }

                    FileUploadButton(
                        text = "Select Vehicles File",
                        fileName = fileName2,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        filePickerLauncher2.launch(arrayOf("*/*"))
                    }
                }


                Spacer(modifier = Modifier.height(20.dp))
                //Button Send to Server
                Button(
                    onClick = {
//                        if (fileUri1 != null && fileUri2 != null) {
//                            isLoading = true
//                            uploadFiles(context, fileUri1!!, fileUri2!!) { tripsList ->
//                                trips = tripsList
//                                isLoading = false
//                            }
//                        } else {
//                            Toast.makeText(context, "Please select both files", Toast.LENGTH_SHORT).show()
//                        }
                    }
                ) {
                    Text(text = "Find Trips")
                }

//                FileUploadButton(
//                    text = "Select Find Trips File",
//                    fileName = fileName3,
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxWidth()
//                ) {
//                    filePickerLauncher3.launch(arrayOf("*/*"))
//                }
//                trips = fileUri3?.let { parseCSVFromUri(context, it) }!!
            }
        }

//        if(isLoading){
//            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
//        } else{
            LazyColumn {
                items(trips) { trip ->
                    TripItem(trip) {
                        val intent = Intent(context, TripDetailActivity::class.java).apply {
                            putExtra("trip", trip)
                        }
                        context.startActivity(intent)
                    }
                }
//            }
        }
    }
}

// Function to parse CSV data from a Uri
//fun parseCSVFromUri(context: Context, uri: Uri): List<Trip> {
//    val trips = mutableListOf<Trip>()
//    try {
//        // Open the InputStream from the Uri
//        val inputStream: InputStream = context.contentResolver.openInputStream(uri)!!
//        val reader = BufferedReader(InputStreamReader(inputStream))
//
//        // Iterate through each line in the CSV file
//        reader.forEachLine { line ->
//            // Assuming each line is a trip entry in CSV format
//            val columns = line.split(",")
//
//            // Extract and map data
//            val tripId = columns[0]
//            val shipmentsJson = columns[1]  // assuming shipments are in JSON-like format
//            val mstDistance = columns[2].toDouble()
//            val tripTime = columns[3].toDouble()
//            val vehicleType = columns[4]
//            val capacityUtilization = columns[5].toDouble()
//            val timeUtilization = columns[6].toDouble()
//            val coverageUtilization = columns[7].toDouble()
//
//            // Deserialize shipments JSON into List<Shipment>
//            val gson = Gson()
//            val shipmentType = object : TypeToken<List<Shipment>>() {}.type
//            val shipments: List<Shipment> = gson.fromJson(shipmentsJson, shipmentType)
//
//            // Create Trip object and add to list
//            val trip = Trip(
//                tripId,
//                shipments,
//                mstDistance,
//                tripTime,
//                vehicleType,
//                capacityUtilization,
//                timeUtilization,
//                coverageUtilization
//            )
//            trips.add(trip)
//        }
//
//    } catch (e: Exception) {
//        Log.e("CSV Parsing", "Error parsing CSV from Uri: ${e.message}")
//    }
//
//    return trips
//}


// Function to parse LatLng from a string like "LatLng(12.34, 56.78)"
//fun parseLatLng(locationStr: String): LatLng {
//    val regex = """LatLng\(([-+]?[0-9]*\.?[0-9]+),\s*([+-]?[0-9]*\.?[0-9]+)\)""".toRegex()
//    val matchResult = regex.find(locationStr)
//    val lat = matchResult?.groups?.get(1)?.value?.toDouble() ?: 0.0
//    val lng = matchResult?.groups?.get(2)?.value?.toDouble() ?: 0.0
//    return LatLng(lat, lng)
//}


fun uploadFiles(context: Context, fileUri1: Uri, fileUri2: Uri, onResult: (List<Trip>) -> Unit) {
    val client = OkHttpClient()

    val file1 = uriToFile(context, fileUri1)
    val file2 = uriToFile(context, fileUri2)

    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file1", file1.name, file1.asRequestBody("text/csv".toMediaTypeOrNull()))
        .addFormDataPart("file2", file2.name, file2.asRequestBody("text/csv".toMediaTypeOrNull()))
        .build()

    val request = Request.Builder()
        .url("https://smartroute-0cw4.onrender.com/predict")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Failed to upload: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onResponse(call: Call, response: okhttp3.Response) {
//            response.body?.let {
//                val jsonResponse = it.string()
//                val tripsList: List<Trip> = Gson().fromJson(jsonResponse, Array<Trip>::class.java).toList()
//
//                (context as? ComponentActivity)?.runOnUiThread {
//                    onResult(tripsList)
//                }
//            }

            val responseBody = response.body?.string()
            if (responseBody != null) {
                try {
                    val jsonArray = JSONArray(responseBody)  // Try parsing as JSON Array
                    val tripsList: List<Trip> = Gson().fromJson(responseBody, Array<Trip>::class.java).toList()
                    (context as? ComponentActivity)?.runOnUiThread {
                        onResult(tripsList)
                    }
                } catch (e: JSONException) {
                    // Handle as string if JSON Array fails
                    val responseData = responseBody
                }
            }

        }
    })
}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("upload", ".csv", context.cacheDir)
    tempFile.outputStream().use { outputStream ->
        inputStream?.copyTo(outputStream)
    }
    return tempFile
}


@Composable
fun TripItem(trip: Trip, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Trip ID: ${trip.tripId}", fontWeight = FontWeight.Bold)
            Text(text = "Vehicle: ${trip.vehicleType}")
            Text(text = "Distance: ${trip.mstDistance} km")
            Text(text = "Time: ${trip.tripTime} hrs")
        }
    }
}


@Parcelize
data class Trip(
    val tripId: String,
    val shipments: List<Shipment>,
    val mstDistance: Double,
    val tripTime: Double,
    val vehicleType: String,
    val capacityUtilization: Double,
    val timeUtilization: Double,
    val coverageUtilization: Double
) : Parcelable

@Parcelize
data class Shipment(
    val id: Int,
    val location: LatLng,
    val timeSlot: String
) : Parcelable


@Composable
fun FileUploadButton(
    text: String,
    fileName: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier // Ensure modifier is used
    ) {
        Text(text = text)
    }
}


@Composable
fun FileUploadButton(label: String, fileName: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onClick) {
            Text(text = label)
        }
        Text(text = fileName, fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
    }
}

private fun getFileName(uri: Uri): String {
    return uri.lastPathSegment?.substringAfterLast("/") ?: "Unknown File"
}