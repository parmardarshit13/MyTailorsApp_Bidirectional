package com.example.mytailorsapp.ui.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.database.InventoryItem  // Assuming InventoryItem is a database entity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(navController: NavController, viewModel: CustomerViewModel = viewModel()) {
    val inventoryItems by viewModel.inventoryItems.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Your Orders") }) }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(inventoryItems) { item ->
                InventoryCard(item)
            }
        }
    }
}

@Composable
fun InventoryCard(item: InventoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Item: ${item.name}", style = MaterialTheme.typography.titleLarge)
            Text("Status: ${item.status}")
            Text("Shop: ${item.shopName}")
            Text("Expected Completion: ${item.expectedDate}")
        }
    }
}
