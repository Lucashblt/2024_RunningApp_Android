package com.example.runningapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.runningapp.Data.UserData.getUserByLoggin
import com.example.runningapp.Data.UserData.verifLogin
import androidx.compose.foundation.background
import androidx.compose.ui.zIndex

@Composable
fun Login(navController: NavHostController, userId: MutableState<Int>) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var essaie by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.runner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (verifLogin(username, password)) {
                        val newUserId = getUserByLoggin(username, password)
                        essaie = false
                        userId.value = newUserId
                        navController.navigate("home/$newUserId")
                    } else {
                        essaie = true
                        username = ""
                        password = ""
                    }
                    /*
                    userId.value = 1
                    navController.navigate("home/1")*/
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Not register yet?")
            }
        }
        if (essaie) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "Incorrect login or password",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(Color.Red.copy(alpha = 0.7f))
                        .padding(8.dp)
                        .zIndex(1f)
                )
            }
        }
    }
}
