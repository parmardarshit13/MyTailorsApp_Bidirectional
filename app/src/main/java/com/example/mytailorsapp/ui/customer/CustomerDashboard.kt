package com.example.mytailorsapp.ui.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mytailorsapp.database.AppDatabase
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory

class CustomerDashboard : ComponentActivity() {
    private val viewModel: CustomerViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        CustomerViewModelFactory(database.customerDao(), database.inventoryDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CustomerDashboardUI(viewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDashboardUI(viewModel: CustomerViewModel, navController: NavController) {
    val customer by viewModel.selectedCustomer.collectAsState()

    // 🔹 Fetch Logged-in Customer ID
    LaunchedEffect(Unit) {
        viewModel.getLoggedInCustomerId { customerId ->
            customerId?.let { viewModel.fetchCustomerById(it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Dashboard") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // 🔹 Welcome Text
            Text(
                text = "Welcome, ${customer?.name ?: "User"}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp)) // Add spacing below the title

            // 🔹 New Row for Icons (Placed Below the Title)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 🔹 View Materials Button (Changed Icon)
                IconButton(onClick = { navController.navigate("material_screen") }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Materials",
                        modifier = Modifier.size(32.dp)
                    )
                }

                // 🔹 Check Inventory Button
                IconButton(onClick = { navController.navigate("inventory_screen") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.List,
                        contentDescription = "Inventory",
                        modifier = Modifier.size(32.dp)
                    )
                }

                // 🔹 Search Shops Icon
                IconButton(onClick = { navController.navigate("shop_search_screen") }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Shops",
                        modifier = Modifier.size(32.dp)
                    )
                }

                // 🔹 Profile Icon (Navigates to Profile)
                customer?.let { customerData ->
                    IconButton(onClick = { navController.navigate("profile_screen/${customerData.id}") }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
