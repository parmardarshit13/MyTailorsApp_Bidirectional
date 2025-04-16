package com.example.mytailorsapp.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytailorsapp.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteInventoryScreen(viewModel: InventoryViewModel) {
    val inventoryList by viewModel.inventoryItems.collectAsState(emptyList()) // ✅ Uses collectAsState for reactivity
    var isAdmin by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Delete Inventory Item") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Select an item to delete", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn {
                items(inventoryList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                viewModel.deleteInventoryItem(item,isAdmin)
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Name: ${item.name}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Type: ${item.type}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Price: $${item.price}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Status: ${item.status}", style = MaterialTheme.typography.bodyMedium)  // ✅ Shows status
                        }
                    }
                }
            }
        }
    }
}
