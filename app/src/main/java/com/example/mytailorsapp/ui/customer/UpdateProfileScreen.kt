package com.example.mytailorsapp.ui.customer

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mytailorsapp.data.models.CustomerEntity
import com.example.mytailorsapp.viewmodel.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    navController: NavController,
    customerId: Int,
    viewModel: CustomerViewModel = viewModel()
) {
    val context = LocalContext.current
    val customer by viewModel.selectedCustomer.collectAsState()

    // 🚨 Don't return early — instead wait till data is available
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    // ✅ Fetch customer on entry
    LaunchedEffect(customerId) {
        viewModel.fetchCustomerById(customerId)
    }

    // ✅ Update fields only once when customer is loaded
    LaunchedEffect(customer) {
        customer?.let {
            name = it.name
            contact = it.contact
            email = it.email
            address = it.address
            password = it.password
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Edit Your Profile", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError,
                supportingText = {
                    if (emailError) Text("Enter a valid email address")
                }
            )
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("New Password") }, modifier = Modifier.fillMaxWidth(), isError = passwordError)
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError,
                supportingText = {
                    if (passwordError) Text("Passwords don't match")
                }
            )

            Button(
                onClick = {
                    passwordError = password != confirmPassword
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

                    if (!passwordError && !emailError) {
                        customer?.id?.let { id ->
                            val updatedCustomer = CustomerEntity(
                                id = id,
                                name = name,
                                contact = contact,
                                email = email,
                                address = address,
                                password = password,
                                isLoggedIn = true // ✅ Maintain login status
                            )
                            viewModel.updateCustomerProfile(updatedCustomer)
                            Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

        }
    }
}
