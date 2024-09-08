package com.example.runningapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.runningapp.Data.ActivityInfo
import com.example.runningapp.Data.Comments
import com.example.runningapp.Data.UserData

@Composable
fun ActivityCardWithComment(userId: Int, activityId: Int, navController: NavHostController) {
    val activity = ActivityInfo.getActivityById(activityId) ?: return
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        ActivityCard(activity, true, userId, navController)
    }
}

@Composable
fun CommentCard(comments: Comments, connectUserId: Int, navController: NavHostController){
    val user = UserData.getUserById(comments.userId) ?: return
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Start)
        ) {
            Image(
                painter = painterResource(id = user.profilePic),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                    .clickable {
                        navController.navigate("profile/${connectUserId}/${user.id}")
                    }
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column (
                modifier = Modifier
                    .fillMaxHeight()
            ){
                Row (
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = user.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = comments.date,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
        Row (
            modifier = Modifier
                .padding(start = 15.dp)
                .align(Alignment.Start)
        ){
            Text(
                text = comments.message,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}