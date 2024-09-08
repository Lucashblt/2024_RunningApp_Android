package com.example.runningapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextField
import androidx.compose.ui.res.painterResource
import com.example.runningapp.Data.ActivityData
import com.example.runningapp.Data.ActivityInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.gson.Gson

@Composable
fun MapPage(userId: Int, navController: NavHostController) {
    var statRun by remember { mutableStateOf("Start") }
    var distance by remember { mutableDoubleStateOf(0.0) }
    var time by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf(GeoPoint(0.0, 0.0)) }
    var locations by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    var selectedSport by remember { mutableStateOf("running.png") }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            coroutineScope.launch {
                while (isRunning) {
                    delay(1000L)
                    time++
                    // distance += 0.01
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RequestLocationPermissions {
            TrackLocationUpdates { newLocation ->
                if (isRunning) {
                    locations = locations + newLocation
                    if (locations.size > 1) {
                        distance += locations[locations.size - 2].distanceToAsDouble(newLocation) / 1000
                    }
                } else {
                    currentLocation = newLocation
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
        ) {
            val boxHeight = maxHeight
            MapViewComposable(boxHeight, locations, currentLocation)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (statRun) {
                "Start" -> {
                    Run(
                        onStateChange = { newState ->
                            statRun = newState
                            isRunning = newState == "Stop" || newState == "Restart"
                        },
                        onSportChange = { newSport ->
                            selectedSport = newSport
                        }
                    )
                }
                "Stop" -> {
                    Stop(distance, time, onStateChange = { newState ->
                            statRun = newState
                            isRunning = !(newState == "Restart" || newState == "End")
                    })
                }
                "Restart" -> {
                    Restart(distance, time, onStateChange = { newState ->
                        statRun = newState
                        isRunning = newState == "Stop"
                    })
                }
                "End" -> {
                    End(
                        distance, time, selectedSport, onStateChange = { newState ->
                            statRun = newState
                            isRunning = false
                        }, userId, navController, locations
                    )
                }
            }
        }
    }
}


@Composable
fun Run(onStateChange: (String) -> Unit, onSportChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedSport by remember { mutableStateOf("running.png") }

    val sports = listOf("running.png", "cycling.png", "swimming.png")
    val sportNames = listOf("Running", "Cycling", "Swimming")
    val sportIcons = listOf(R.drawable.running, R.drawable.cycling, R.drawable.swimming)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { expanded = true }) {
                Text(text = "Select Sport")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = when (selectedSport) {
                    "cycling.png" -> R.drawable.cycling
                    "swimming.png" -> R.drawable.swimming
                    else -> R.drawable.running
                }),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sports.forEachIndexed { index, sport ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = sportNames[index])
                                Spacer(modifier = Modifier.width(8.dp))
                                Image(painter = painterResource(id = sportIcons[index]), contentDescription = null, modifier = Modifier.size(30.dp))
                            }
                        },
                        onClick = {
                            selectedSport = sport
                            expanded = false
                            onSportChange(sport)
                        }
                    )
                }
            }
        }
        Row {
            Button(
                onClick = { onStateChange("Stop") },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(150.dp, 64.dp)
            ) {
                Text(text = "Start")
            }
        }
    }
}

@Composable
fun Stop(distance: Double, time: Int, onStateChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            DisplayInfoRun(distance, time)
        }
        Row {
            Button(
                onClick = { onStateChange("Restart") },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(150.dp, 64.dp)
            ) {
                Text(text = "Stop")
            }
            Button(
                onClick = { onStateChange("End") },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(150.dp, 64.dp)
            ) {
                Text(text = "End")
            }
        }
    }
}

@Composable
fun Restart(distance: Double, time: Int, onStateChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            DisplayInfoRun(distance, time)
        }
        Row {
            Button(
                onClick = { onStateChange("Stop") },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(150.dp, 64.dp)
            ) {
                Text(text = "Restart")
            }
            Button(
                onClick = { onStateChange("End") },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(150.dp, 64.dp)
            ) {
                Text(text = "End")
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun End(
    distance: Double,
    time: Int,
    sport: String,
    onStateChange: (String) -> Unit,
    userId: Int,
    navController: NavHostController,
    locations: List<GeoPoint>
) {
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Save your run") },
            text = {
                Column {
                    Text("Enter a message for your run:")
                    TextField(
                        value = message,
                        onValueChange = { message = it }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val formattedDistance = String.format("%.2f", distance).toDouble()
                        val routeData = Gson().toJson(locations)
                        val newActivity = ActivityData(
                            id = ActivityInfo.getActivities().size + 1,
                            userId = userId,
                            likes = 0,
                            distance = formattedDistance,
                            time = time,
                            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                            sport = when (sport) {
                                "cycling.png" -> ActivityInfo.Sport.Cycling
                                "swimming.png" -> ActivityInfo.Sport.Swimming
                                else -> ActivityInfo.Sport.Running
                            },
                            map = R.drawable.tracer,
                            message = message,
                            routeData = routeData
                        )
                        ActivityInfo.add(newActivity)
                        showDialog = false
                        navController.navigate("profile/$userId/$userId")
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            DisplayInfoRun(distance, time)
        }
        Row {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(150.dp, 64.dp)
            ) {
                Text(text = "Save the run")
            }
            Button(
                onClick = { navController.navigate("maps/$userId") },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(150.dp, 64.dp)
            ) {
                Text(text = "Delete the run")
            }
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun DisplayInfoRun(distance: Double, time: Int) {
    val hours = time / 3600
    val minutes = (time % 3600) / 60
    val seconds = time % 60
    val timeString = "${hours}h ${minutes}m ${seconds}s"
    val distanceString = String.format("%.2f", distance)
    Row {
        ActivityDetailColumn("Distance", "$distanceString km")
        Spacer(modifier = Modifier.width(12.dp))
        ActivityDetailColumn("Time", timeString)
        Spacer(modifier = Modifier.width(12.dp))
        val pace = ActivityInfo.calculatePace(distance, time)
        ActivityDetailColumn("Pace", "$pace ")
    }
}

