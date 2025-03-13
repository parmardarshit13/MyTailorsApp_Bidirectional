package com.example.mytailorsapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageInventoryScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Manage Inventory") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Inventory Management Feature (To be implemented)", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
