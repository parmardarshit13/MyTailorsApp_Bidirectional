package com.example.mytailorsapp.ui.auth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.database.AppDatabase
import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.mytailorsapp.database.CustomerEntity
import kotlinx.coroutines.launch
import com.example.mytailorsapp.R

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
    var userType by remember { mutableStateOf("customer") } // Default user type
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            coroutineScope.launch { snackbarHostState.showSnackbar(it) }
            snackbarMessage = null
        }
    }

    // 🔹 Background Image Support
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_auth_background_02), // Add tailor-related image to `res/drawable`
            contentDescription = "Tailor Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Register", style = TextStyle(fontSize = 24.sp)) },
                    modifier = Modifier.padding(top = 16.dp) // Move title slightly down
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()), // ✅ Enable scrolling
                verticalArrangement = Arrangement.Center
            ) {
                // ✅ All input fields (same as before)
                OutlinedTextField(value = name, onValueChange = { name = it.trim() }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = contact, onValueChange = { contact = it.trim() }, label = { Text("Contact Number") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = email, onValueChange = { email = it.trim() }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = address, onValueChange = { address = it.trim() }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                // ✅ User Type Dropdown (unchanged)
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
                        DropdownMenuItem(text = { Text("Customer") }, onClick = { userType = "customer"; expanded = false })
                        DropdownMenuItem(text = { Text("Tailor") }, onClick = { userType = "tailor"; expanded = false })
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // ✅ Password Fields
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Register Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            // 🔹 Validation Checks
                            if (name.isBlank() || contact.isBlank() || email.isBlank() || address.isBlank() || password.isBlank()) {
                                snackbarMessage = "All fields are required!"
                                return@launch
                            }
                            if (!contact.matches(Regex("^[0-9]{10}$"))) {
                                snackbarMessage = "Enter a valid 10-digit contact number"
                                return@launch
                            }
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                snackbarMessage = "Enter a valid email address"
                                return@launch
                            }
                            if (password.length < 6) {
                                snackbarMessage = "Password must be at least 6 characters"
                                return@launch
                            }
                            if (password != confirmPassword) {
                                snackbarMessage = "Passwords do not match"
                                return@launch
                            }

                            // 🔹 Check if Email Already Exists
                            val existingCustomer = customerDao.getCustomerByEmail(email)
                            if (existingCustomer != null) {
                                snackbarMessage = "Email already registered!"
                                return@launch
                            }

                            // 🔹 Register Customer
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
                            navController?.navigate("login_screen")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }
    }
}
