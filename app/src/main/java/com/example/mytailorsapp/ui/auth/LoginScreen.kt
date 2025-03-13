package com.example.mytailorsapp.ui.auth

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mytailorsapp.database.AppDatabase
import kotlinx.coroutines.launch
import com.example.mytailorsapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenUI(navController: NavController?, context: Context) {
    val customerDao = remember(context) { AppDatabase.getDatabase(context).customerDao() }
    val workerDao = remember(context) { AppDatabase.getDatabase(context).workerDao() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 🔹 Snackbar Handling
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            coroutineScope.launch { snackbarHostState.showSnackbar(it) }
            snackbarMessage = null
        }
    }

    // 🔹 Background Image Fix (Added Alignment)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_auth_background_01),
            contentDescription = "Tailor Background",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center), // ✅ Ensure proper alignment
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Login", style = TextStyle(fontSize = 24.sp)) },
                    modifier = Modifier.padding(top = 16.dp)
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier.padding(top = 8.dp)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                // ✅ Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // ✅ Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // ✅ Login Button (Fixed Crash Issues)
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val isAdmin = email == "admin@gmail.com" && password == "admin123"
                            var isCustomer = false
                            var isWorker = false

                            if (!isAdmin) {
                                val customer = withContext(Dispatchers.IO) {
                                    customerDao.authenticateUser(email, password)
                                }
                                isCustomer = customer != null

                                if (!isCustomer) {
                                    val worker = withContext(Dispatchers.IO) {
                                        workerDao.authenticateWorker(email, password)
                                    }
                                    isWorker = worker != null && worker.userType == "worker"
                                }
                            }

                            when {
                                isAdmin -> navController?.navigate("admin_dashboard")
                                isCustomer -> {
                                    withContext(Dispatchers.IO) { customerDao.updateLoginStatus(email, true) }
                                    navController?.navigate("customer_dashboard")
                                }
                                isWorker -> {
                                    withContext(Dispatchers.IO) { workerDao.updateLoginStatus(email, true) }
                                    navController?.navigate("worker_dashboard") // for future
                                }
                                else -> snackbarMessage = "Invalid email or password"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ✅ Register & Forgot Password Links
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
