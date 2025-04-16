package com.example.mytailorsapp.ui.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.data.models.InventoryStatus
import com.example.mytailorsapp.viewmodel.InventoryViewModel

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInventoryScreen(
    navController: NavController,
    viewModel: InventoryViewModel,
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(InventoryStatus.IN_PROGRESS) } // ✅ Default status
    var isAdmin by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Inventory Item") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
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
            Spacer(modifier = Modifier.height(10.dp))

            // ✅ Dropdown for Status Selection
            var expanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Status: ${status.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    InventoryStatus.entries.forEach { statusOption ->
                        DropdownMenuItem(
                            text = { Text(statusOption.name) },
                            onClick = {
                                status = statusOption
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && type.isNotEmpty() && price.isNotEmpty()) {
                        val newItem = InventoryItem(
                            name = name,
                            type = type,
                            price = price.toDoubleOrNull() ?: 0.0,
                            status = status // ✅ Enum Used
                        )

                        viewModel.addInventoryItem(newItem,isAdmin)
                        navController.popBackStack() // ✅ Navigate back after adding
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotEmpty() && type.isNotEmpty() && price.isNotEmpty()
            ) {
                Text("Add Item")
            }
        }
    }
}
