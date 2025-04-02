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
        WorkerViewModelFactory(AppDatabase.getDatabase(applicationContext).workerDao())
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
    LaunchedEffect(Unit) { viewModel.fetchAllWorkers() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Admin Dashboard") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Welcome, Admin", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Navigation Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val navItems = listOf(
                    R.drawable.ic_manage_workers to "manage_workers_screen",
                    R.drawable.ic_manage_shops to "manage_shops_screen",
                    R.drawable.ic_manage_inventory to "manage_inventory_screen",
                    R.drawable.ic_admin_profile to "admin_profile_screen",
                    null to "worker_search_screen" // Explicitly use null for missing icon
                )

                navItems.forEach { (icon, route) ->
                    IconButton(onClick = { navController.navigate(route) }) {
                        if (icon != null) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = route,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}
