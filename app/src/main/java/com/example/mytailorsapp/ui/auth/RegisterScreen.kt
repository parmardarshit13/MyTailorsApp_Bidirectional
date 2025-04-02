package com.example.mytailorsapp.ui.auth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mytailorsapp.database.AppDatabase
import com.example.mytailorsapp.database.CustomerEntity
import kotlinx.coroutines.launch
import com.example.mytailorsapp.R
import android.content.Context
import android.util.Patterns

class RegisterScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreenUI(null, this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenUI(navController: NavController?, context: Context) {
    val customerDao = remember { AppDatabase.getDatabase(context).customerDao() }

    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("Customer") } // Default user type
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            coroutineScope.launch { snackbarHostState.showSnackbar(it) }
            snackbarMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_auth_background_02),
            contentDescription = "Tailor Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Register", fontSize = 22.sp) },
                    modifier = Modifier.padding(top = 16.dp)
                )
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
                OutlinedTextField(value = name, onValueChange = { name = it.trim() }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = contact, onValueChange = { contact = it.trim() }, label = { Text("Contact Number") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = email, onValueChange = { email = it.trim() }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = address, onValueChange = { address = it.trim() }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = userType, onValueChange = { },
                        label = { Text("User Type") }, readOnly = true, modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                            }
                        }
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("Customer") }, onClick = { userType = "Customer"; expanded = false })
                        DropdownMenuItem(text = { Text("Tailor") }, onClick = { userType = "Tailor"; expanded = false })
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            if (name.isBlank() || contact.isBlank() || email.isBlank() || address.isBlank() || password.isBlank()) {
                                snackbarMessage = "All fields are required!"
                                isLoading = false
                                return@launch
                            }
                            if (!contact.matches(Regex("^[0-9]{10}$"))) {
                                snackbarMessage = "Enter a valid 10-digit contact number"
                                isLoading = false
                                return@launch
                            }
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                snackbarMessage = "Enter a valid email address"
                                isLoading = false
                                return@launch
                            }
                            if (password.length < 6) {
                                snackbarMessage = "Password must be at least 6 characters"
                                isLoading = false
                                return@launch
                            }
                            if (password != confirmPassword) {
                                snackbarMessage = "Passwords do not match"
                                isLoading = false
                                return@launch
                            }

                            val existingCustomer = customerDao.getCustomerByEmail(email)
                            if (existingCustomer != null) {
                                snackbarMessage = "Email already registered!"
                                isLoading = false
                                return@launch
                            }

                            val newCustomer = CustomerEntity(
                                name = name,
                                contact = contact,
                                email = email,
                                address = address,
                                password = password,
                                userType = userType,
                                isLoggedIn = false
                            )
                            customerDao.registerCustomer(newCustomer)

                            snackbarMessage = "Registration Successful!"
                            isLoading = false
                            navController?.navigate("login_screen")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Register")
                    }
                }
            }
        }
    }
}
