package com.example.runningapp

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeContent(userId:  Int,navController: NavHostController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        ActivityContent(userId = userId,navController)
    }
}