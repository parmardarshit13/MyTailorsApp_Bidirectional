package com.example.mytailorsapp.ui.auth

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mytailorsapp.MyTailorsApp
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.AdminRepository
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory
import kotlinx.coroutines.launch
import com.example.mytailorsapp.data.repository.CustomerRepository
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.data.repository.MaterialRepository
import com.example.mytailorsapp.viewmodel.AdminViewModel
import com.example.mytailorsapp.viewmodel.AdminViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenUI(navController: NavController?, context: Context = LocalContext.current) {
    context.applicationContext as MyTailorsApp
    val viewModel: CustomerViewModel = viewModel(
        factory = CustomerViewModelFactory(
            CustomerRepository(),
            MaterialRepository(),
            InventoryRepository()
        )
    )

    val adminViewModel: AdminViewModel = viewModel(
        factory = AdminViewModelFactory(AdminRepository())
    )

    val snackbarHostState = remember { SnackbarHostState() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Handle snackbar messages
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_auth_background_01),
            contentDescription = "Tailor Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Login", style = TextStyle(fontSize = 24.sp)) })
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Login Button
                Button(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            snackbarMessage = "Please fill all fields"
                            return@Button
                        }

                        // ✅ Try Admin login via repository
                        adminViewModel.authenticateAdmin(email, password) { isAdmin ->
                            if (isAdmin) {
                                navController?.navigate("admin_dashboard") {
                                    popUpTo("login_screen") { inclusive = true }
                                }
                            } else {
                                // continue with customer login
                                isLoading = true
                                viewModel.viewModelScope.launch {
                                    try {
                                        val success = viewModel.login(email, password)
                                        if (success) {
                                            navController?.navigate("customer_dashboard") {
                                                popUpTo("login_screen") { inclusive = true }
                                            }
                                        } else {
                                            snackbarMessage = "Invalid email or password"
                                        }
                                    } catch (e: Exception) {
                                        snackbarMessage = "Login failed: ${e.message}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Login")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Register & Forgot Password Links
                TextButton(onClick = { navController?.navigate("register_screen") }) {
                    Text("Don't have an account? Register")
                }
                TextButton(onClick = { navController?.navigate("forgot_password_screen") }) {
                    Text("Forgot Password?")
                }
            }
        }
    }
}