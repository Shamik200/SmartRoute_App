package com.example.smartroute

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TripDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TripDetailScreen()
        }
    }
}

@Composable
fun TripDetailScreen() {
    val context = LocalContext.current
    val trip = (context as? Activity)?.intent?.getParcelableExtra<Trip>("trip")

    trip?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Make the screen scrollable
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title Section
            Text(
                text = "Trip Details",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Vehicle Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Vehicle: ${it.vehicleType}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Trip ID: ${it.tripId}", fontSize = 16.sp)
                    Text(text = "Time: ${it.tripTime}", fontSize = 16.sp)
                    Text(text = "Distance: ${it.mstDistance} km", fontSize = 16.sp)
                    Text(text = "Capacity Utilization: ${it.capacityUtilization}%", fontSize = 16.sp)
                    Text(text = "Time Utilization: ${it.timeUtilization}%", fontSize = 16.sp)
                    Text(text = "Coverage Utilization: ${it.coverageUtilization}%", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Map and Directions Section
            val source = Pair(19.075887, 72.877911) // Example source
            MapScreen(source = source, shipments = it.shipments)

            Spacer(modifier = Modifier.height(20.dp))

            // Button to open map in Google Maps

        }
    }
}

@Composable
fun MapScreen(source: Pair<Double, Double>, shipments: List<Shipment>) {
    val context = LocalContext.current

    // Assuming Location is a data class with latitude and longitude

    val waypoints = shipments.map { Pair(it.location.latitude, it.location.longitude) }

    Button(onClick = { openGoogleMapsWithRoute(context, source, waypoints) }) {
        Text("Open Route in Google Maps")
    }
}

fun openGoogleMapsWithRoute(context: Context, source: Pair<Double, Double>, waypoints: List<Pair<Double, Double>>) {
    if (waypoints.isEmpty()) return

    val origin = "${source.first},${source.second}"
    val destination = origin // Returning back to source

    val waypointList = waypoints.joinToString("|") { "${it.first},${it.second}" }

    val baseUri = StringBuilder("https://www.google.com/maps/dir/?api=1&travelmode=driving")
    baseUri.append("&origin=$origin")
    baseUri.append("&destination=$destination")

    if (waypointList.isNotEmpty()) {
        baseUri.append("&waypoints=$waypointList") // Correctly formatted waypoints
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(baseUri.toString()))
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}