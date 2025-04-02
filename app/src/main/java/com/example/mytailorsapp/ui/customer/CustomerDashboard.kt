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
        CustomerViewModelFactory(
            AppDatabase.getDatabase(applicationContext).customerDao(),
            AppDatabase.getDatabase(applicationContext).materialDao(),
            AppDatabase.getDatabase(applicationContext).inventoryDao()
        )
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

    LaunchedEffect(Unit) {
        viewModel.getLoggedInCustomerId { customerId ->
            customerId?.let { viewModel.fetchCustomerById(it) }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Customer Dashboard") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Welcome, ${customer?.name ?: "User"}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Navigation Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Define navigation items correctly as pairs
                val navItems = listOf(
                    Pair(Icons.Default.ShoppingCart, "material_screen"),
                    Pair(Icons.AutoMirrored.Filled.List, "inventory_screen"),
                    Pair(Icons.Default.Search, "shop_search_screen"),
                    Pair(Icons.Default.AccountCircle, "profile_screen/${customer?.id}")
                )

                // Iterate correctly over pairs (icon, route)
                navItems.forEach { (icon, route) ->
                    IconButton(onClick = { navController.navigate(route) }) {
                        Icon(imageVector = icon, contentDescription = route, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
    }
}
