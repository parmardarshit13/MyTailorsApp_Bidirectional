package com.example.mytailorsapp.ui.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mytailorsapp.database.AppDatabase
import com.example.mytailorsapp.viewmodel.WorkerViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModelFactory
import com.example.mytailorsapp.R

class AdminDashboard : ComponentActivity() {
    private val viewModel: WorkerViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        WorkerViewModelFactory(database.workerDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AdminDashboardUI(viewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardUI(viewModel: WorkerViewModel, navController: NavController) {
    LaunchedEffect(Unit) {
        viewModel.fetchAllWorkers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {// 🔹 Welcome Text
            Text(
                text = "Welcome, Admin",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp)) // Space between title & navbar

            // 🔹 Navigation Bar (Moved Below Title)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly // Distribute icons evenly
            ) {
                IconButton(onClick = { navController.navigate("manage_workers_screen") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_manage_workers),
                        contentDescription = "Manage Workers",
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { navController.navigate("manage_shops_screen") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_manage_shops),
                        contentDescription = "Manage Shops",
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { navController.navigate("manage_inventory_screen") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_manage_inventory),
                        contentDescription = "Manage Inventory",
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { navController.navigate("admin_profile_screen") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_admin_profile),
                        contentDescription = "Admin Profile",
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { navController.navigate("worker_search_screen") }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Workers",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
