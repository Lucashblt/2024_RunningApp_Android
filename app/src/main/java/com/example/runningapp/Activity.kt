package com.example.runningapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
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
import com.example.runningapp.Data.CommentData
import com.example.runningapp.Data.Comments
import com.example.runningapp.Data.UserData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityContent(userId: Int, navController: NavHostController) {
    val activities = ActivityInfo.getActivities()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        activities.forEach { activity ->
            ActivityCard(activity, false, userId, navController)
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ActivityCard(activity: ActivityData, comments: Boolean, userId: Int, navController: NavHostController) {

    val user = UserData.getUserById(activity.userId) ?: return

    var likes by remember { mutableIntStateOf(activity.likes) }
    var liked by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!comments) {
                    navController.navigate("activity/$userId/${activity.id}")
                }
            }
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Start)
            ) {
                Image(
                    painter = painterResource(id = user.profilePic),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                        .clickable {
                            navController.navigate("profile/$userId/${user.id}")
                        }
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = activity.sport.icon),
                            contentDescription = "${activity.sport.name} Icon",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(top = 4.dp, end = 4.dp)
                        )
                        Text(
                            text = "${activity.sport.name} ${activity.date}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .align(Alignment.Start)
            ) {
                Text(
                    text = activity.message,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
            Row(modifier = Modifier.padding(start = 15.dp)) {
                ActivityDetailColumn("Distance", "${activity.distance} km")
                Spacer(modifier = Modifier.width(12.dp))
                val hours = activity.time / 3600
                val minutes = (activity.time % 3600) / 60
                val seconds = activity.time % 60
                val timeString = "${hours}h ${minutes}m ${seconds}s"
                ActivityDetailColumn("Time", timeString)
                Spacer(modifier = Modifier.width(12.dp))
                val pace = ActivityInfo.calculatePace(activity.distance, activity.time)
                ActivityDetailColumn("Pace", "$pace ")
            }
            if (activity.routeData == null) {
                Image(
                    painter = painterResource(id = activity.map),
                    contentDescription = "Map",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Spacer(modifier = Modifier.height(80.dp))
                RouteMapComposable(false, null, navController, routeData = activity.routeData)
                Spacer(modifier = Modifier.height(80.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        if (liked) {
                            likes--
                            liked = false
                        } else {
                            likes++
                            liked = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (liked) Color.Red else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (liked) "Unlike" else "Like",
                        fontSize = 20.sp
                    )
                }
                Text(
                    text = "$likes",
                    fontSize = 35.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            if(comments){
                val commentsList = CommentData.getCommentsByActivityId(activity.id)
                Text(
                    text = "Comments :",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
                var newComment by remember { mutableStateOf("") }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ) {
                    TextField(
                        value = newComment,
                        onValueChange = { newComment = it },
                        label = { Text("Add a comment") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val current = formatter.format(Date())

                    Button(
                        onClick = {
                            if (newComment.isNotBlank()) {
                                CommentData.addComment(
                                    Comments(
                                        id = (commentsList.maxOfOrNull { it.id } ?: 0) + 1,
                                        userId = userId,
                                        activityId = activity.id,
                                        date = current,
                                        message = newComment
                                    )
                                )
                                newComment = ""
                            }
                        }
                    ) {
                        Text("Submit")
                    }
                }
                commentsList.forEach { comment ->
                    CommentCard(comment, userId, navController)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun ActivityDetailColumn(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
        )
    }
}