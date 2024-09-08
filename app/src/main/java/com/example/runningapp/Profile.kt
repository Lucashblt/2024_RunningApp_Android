package com.example.runningapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.example.runningapp.Data.ActivityData
import com.example.runningapp.Data.ActivityInfo
import com.example.runningapp.Data.UserData

@Composable
fun Profile(userId: Int, profileUserId: Int, navController: NavHostController) {
    val user = UserData.getUserById(profileUserId) ?: return
    val activitiesList = ActivityInfo.getActivitiesByUserId(profileUserId)

    var showActivities by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .align(Alignment.Start)
                .border(1.dp, color = Color.Black, RoundedCornerShape(16.dp))
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = user.profilePic),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    text = "${user.name} ${user.surname}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { showActivities = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showActivities) Color.Gray else Color.LightGray
                )
            ) {
                Text(text = "Activities")
            }
            Button(
                onClick = { showActivities = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!showActivities) Color.Gray else Color.LightGray
                )
            ) {
                Text(text = "Statistics")
            }
        }
        if (showActivities) {
            ActivitiesSection(activitiesList, navController, userId)
        } else {
            StatisticsSection(activitiesList, navController,userId)
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun StatisticsSection(activities: List<ActivityData>, navController: NavHostController, userId: Int) {
    val sports = ActivityInfo.Sport.entries.toTypedArray()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        sports.forEach { sport ->
            val sportActivities = activities.filter { it.sport == sport }

            val totalDistance = sportActivities.sumOf { it.distance }
            val totalTime = sportActivities.sumOf { it.time }
            val bestActivity = sportActivities.maxByOrNull { it.distance }
            val bestPaceActivity = sportActivities.minByOrNull { it.time / it.distance }

            SportStatistics(
                title = "${sport.name} Stats Totals",
                totalDistance = totalDistance,
                totalTime = totalTime,
                activityCount = sportActivities.size,
                bestActivity = bestActivity,
                bestPaceActivity = bestPaceActivity,
                navController = navController,
                userId = userId
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SportStatistics(
    title: String,
    totalDistance: Double,
    totalTime: Int,
    activityCount: Int,
    bestActivity: ActivityData?,
    bestPaceActivity: ActivityData?,
    navController: NavHostController,
    userId: Int
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .padding(10.dp)
        ){
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row (
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ){
                ActivityDetail("Distance: ", "$totalDistance km")
                val hours = totalTime / 3600
                val minutes = (totalTime % 3600) / 60
                val seconds = totalTime % 60
                val timeString = "${hours}h ${minutes}m ${seconds}s"
                ActivityDetail("Time: ", timeString)
                ActivityDetail("Activities", "$activityCount" )
            }

            Spacer(modifier = Modifier.height(8.dp))

            bestActivity?.let {
                HighlightedStat(
                    label = "Longest Activity",
                    value = "${it.distance} km",
                    onClick = { navController.navigate("activity/$userId/${it.id}") }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            bestPaceActivity?.let {
                val pace = ActivityInfo.calculatePace(it.distance, it.time)
                HighlightedStat(
                    label = "Best Pace",
                    value = pace,
                    onClick = { navController.navigate("activity/$userId/${it.id}") }
                )
            }
        }
    }
}

@Composable
fun HighlightedStat(label: String, value: String, onClick: () -> Unit){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .border(2.dp, Color.Gray, MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .background(Color.LightGray, MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column (
            modifier = Modifier
                .padding(4.dp)
        ){
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
@Composable
fun ActivityDetail(label : String, value : String){
    Column (
        modifier = Modifier
            .padding(4.dp)
    ){
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun ActivitiesSection(activities: List<ActivityData>, navController: NavHostController, userId: Int) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ){
        activities.chunked(2).forEach { activityPair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                activityPair.forEach { activity ->
                    ActivityMap(userId,activity, navController)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ActivityMap(userId: Int, activity: ActivityData, navController: NavHostController) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(175.dp)
            .padding(2.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(16.dp))
    ) {
        if (activity.routeData == null) {
            Image(
                painter = painterResource(id = activity.map),
                contentDescription = "Activity Map",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        navController.navigate("activity/$userId/${activity.id}")
                    }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val route = "activity/$userId/${activity.id}"
                RouteMapComposable(true, route, navController, routeData = activity.routeData)
            }
        }
    }
}


