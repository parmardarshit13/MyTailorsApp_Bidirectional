package com.example.mytailorsapp.ui.auth

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.database.AppDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreenUI(navController: NavController?, context: Context) {
    val customerDao = remember { AppDatabase.getDatabase(context).customerDao() }
    var email by remember { mutableStateOf("") }  // ✅ Updated from `username` to `email`
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // ✅ Snackbar handling
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            coroutineScope.launch { snackbarHostState.showSnackbar(it) }
            snackbarMessage = null
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Forgot Password") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // 🔹 Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim() }, // ✅ Trim spaces
                label = { Text("Enter Email") }, // ✅ Updated label
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 New Password Input
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Confirm Password Input
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Reset Password Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (email.isBlank() || newPassword.isBlank()) {
                            snackbarMessage = "Fields cannot be empty"
                            return@launch
                        }
                        if (newPassword.length < 6) {
                            snackbarMessage = "Password must be at least 6 characters"
                            return@launch
                        }
                        if (newPassword != confirmPassword) {
                            snackbarMessage = "Passwords do not match"
                            return@launch
                        }
                        val user = customerDao.getUserByEmail(email)  // ✅ Updated method
                        if (user != null) {
                            customerDao.updatePassword(email, newPassword)
                            snackbarMessage = "Password Reset Successful!"
                            navController?.navigate("login_screen") // ✅ Navigate after Snackbar shows
                        } else {
                            snackbarMessage = "User not found!"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset Password")
            }
        }
    }
}
