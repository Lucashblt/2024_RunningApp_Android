package com.example.runningapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@Composable
fun NavBar(navController: NavHostController, userId: MutableState<Int>) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFFB0B0B0))
    ) {
        IconButton(
            onClick = { navController.navigate("home/${userId.value}") }
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Home"
            )
        }
        IconButton(
            onClick = { navController.navigate("maps/${userId.value}") }
        ) {
            Icon(
                imageVector = Icons.Filled.Place,
                contentDescription = "Maps"
            )
        }
        IconButton(
            onClick = { navController.navigate("profile/${userId.value}/${userId.value}") } // Use userId and some profile ID
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile"
            )
        }
    }
}
