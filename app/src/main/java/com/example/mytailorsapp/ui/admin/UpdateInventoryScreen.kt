package com.example.mytailorsapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateInventoryScreen(
    navController: NavController,
    viewModel: InventoryViewModel = viewModel(),
    inventoryName: String? // Accepting item name from navigation
) {
    var searchName by remember { mutableStateOf(inventoryName ?: "") }
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var itemFound by remember { mutableStateOf(false) }

    // Fetch item details automatically if name is passed
    LaunchedEffect(searchName) {
        if (!searchName.isNullOrEmpty()) {
            val item = viewModel.getInventoryItemByName(searchName).first()
            if (item != null) {
                name = item.name
                type = item.type
                price = item.price.toString()
                itemFound = true
            } else {
                itemFound = false
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Update Inventory Item") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Step 1: Get Item Name from User
            TextField(
                value = searchName,
                onValueChange = { searchName = it },
                label = { Text("Enter Item Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = inventoryName.isNullOrEmpty() // Disable editing if name is passed
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Fetch item only if name is manually entered
            if (inventoryName.isNullOrEmpty()) {
                Button(
                    onClick = {
                        runBlocking {
                            val item = viewModel.getInventoryItemByName(searchName).first()
                            if (item != null) {
                                name = item.name
                                type = item.type
                                price = item.price.toString()
                                itemFound = true
                            } else {
                                itemFound = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Find Item")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step 2: Show Details if Item is Found
            if (itemFound) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Item Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        runBlocking {
                            val item = viewModel.getInventoryItemByName(searchName).first()
                            if (item != null) {
                                val updatedItem = item.copy(
                                    name = name,
                                    type = type,
                                    price = price.toDoubleOrNull() ?: 0.0
                                )
                                viewModel.updateInventoryItem(updatedItem)
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Item")
                }
            } else {
                if (searchName.isNotEmpty() && inventoryName.isNullOrEmpty()) {
                    Text("Item not found!", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
