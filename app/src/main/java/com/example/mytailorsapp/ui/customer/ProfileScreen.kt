package com.example.mytailorsapp.ui.customer

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mytailorsapp.database.CustomerEntity
import com.example.mytailorsapp.viewmodel.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    customerId: Int, // Receive customer ID from navigation
    viewModel: CustomerViewModel = viewModel()
) {
    val context = LocalContext.current

    // 🔹 Fetch customer details when screen loads
    LaunchedEffect(customerId) {
        viewModel.fetchCustomerById(customerId)
    }

    // 🔹 Collect customer data from StateFlow
    val customer by viewModel.selectedCustomer.collectAsState()

    // 🔹 Ensure non-null values
    val safeCustomer = customer ?: CustomerEntity(0, "", "", "", "", "", "")

    // 🔹 Remember state variables for editing
    var name by remember { mutableStateOf(safeCustomer.name) }
    var contact by remember { mutableStateOf(safeCustomer.contact) }
    var email by remember { mutableStateOf(safeCustomer.email) }
    var address by remember { mutableStateOf(safeCustomer.address) }
    var password by remember { mutableStateOf(safeCustomer.password) }

    // 🔹 Update UI state when customer data changes
    LaunchedEffect(safeCustomer) {
        name = safeCustomer.name
        contact = safeCustomer.contact
        email = safeCustomer.email
        address = safeCustomer.address
        password = safeCustomer.password
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Update Your Profile", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )

            OutlinedTextField(
                value = contact,
                onValueChange = { contact = it },
                label = { Text("Contact") }
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") }
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )

            Button(
                onClick = {
                    viewModel.updateCustomerProfile(name, contact, email, address, password)
                    Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}
