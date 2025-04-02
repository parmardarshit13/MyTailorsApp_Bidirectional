package com.example.mytailorsapp.ui.auth

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mytailorsapp.database.AppDatabase
import com.example.mytailorsapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenUI(navController: NavController?, context: Context) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // ✅ Room Database Access should be done inside a coroutine
    val db = AppDatabase.getDatabase(context)
    val customerDao = db.customerDao()
    val workerDao = db.workerDao()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    // 🔹 Snackbar Handling
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            coroutineScope.launch { snackbarHostState.showSnackbar(it) }
            snackbarMessage = null
        }
    }

    // 🔹 Background Image Fix
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

                // ✅ Login Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val isAdmin = email == "admin@gmail.com" && password == "admin123"
                            var isCustomer = false
                            var isWorker = false

                            if (!isAdmin) {
                                val customer = withContext(Dispatchers.IO) {
                                    customerDao.authenticate(email, password)
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
                                    navController?.navigate("worker_dashboard")
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
