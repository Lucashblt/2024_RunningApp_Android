package com.example.runningapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.runningapp.Data.UserData

@Composable
fun Register(navController: NavHostController, userId: MutableState<Int>) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
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
            TitleText("Register")
            Spacer(modifier = Modifier.height(16.dp))
            RegisterTextField(value = name, label = "Name", onValueChange = { name = it })
            Spacer(modifier = Modifier.height(16.dp))
            RegisterTextField(value = surname, label = "Surname", onValueChange = { surname = it })
            Spacer(modifier = Modifier.height(16.dp))
            RegisterTextField(value = username, label = "Username", onValueChange = { username = it })
            Spacer(modifier = Modifier.height(16.dp))
            RegisterTextField(value = dateOfBirth, label = "Date of Birth", onValueChange = { dateOfBirth = it })
            Spacer(modifier = Modifier.height(16.dp))
            RegisterTextField(value = password, label = "Password", onValueChange = { password = it }, isPassword = true)
            Spacer(modifier = Modifier.height(16.dp))
            RegisterButton(
                text = "Register",
                enabled = name.isNotEmpty() && surname.isNotEmpty() && username.isNotEmpty() && dateOfBirth.isNotEmpty() && password.isNotEmpty(),
                onClick = {
                    UserData.addUser(name, surname, username, dateOfBirth, password)
                    val newUserId = UserData.getUserByLoggin(username, password)
                    userId.value = newUserId
                    navController.navigate("home/$newUserId")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            RegisterButton(
                text = "Already register ?",
                onClick = { navController.navigate("login") }
            )

        }
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(8.dp)
    )
}

@Composable
fun RegisterTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun RegisterButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}
