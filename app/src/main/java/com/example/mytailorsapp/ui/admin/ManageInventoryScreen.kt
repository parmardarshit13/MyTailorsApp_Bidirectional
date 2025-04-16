package com.example.mytailorsapp.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.ui.common.SidebarDrawer
import com.example.mytailorsapp.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageInventoryScreen(
    navController: NavController,
    inventoryViewModel: InventoryViewModel,
    userId: Int = 0,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val inventoryItems by inventoryViewModel.inventoryItems.collectAsState(initial = emptyList())

    var showMenu by remember { mutableStateOf(false) }
    var selectedItemName by remember { mutableStateOf<String?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // ✅ Use centralized sidebar here
            SidebarDrawer(
                drawerState = drawerState,
                navController = navController,
                selectedIndex = selectedItem,
                onSelect = { selectedItem = it },
                scope = scope,
                isDark = isDark,
                onToggleTheme = onToggleTheme,
                userId = userId,
                isAdminDashboard = true // ✅ default for customers
            )
        }
    ){
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Manage Inventory") })
            },
            floatingActionButton = {
                Box(contentAlignment = Alignment.Center) {
                    FloatingActionButton(onClick = { showMenu = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Inventory Task")
                    }

                    // Show Dropdown Menu when FAB is clicked
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(text = { Text("Add Inventory") }, onClick = {
                            showMenu = false
                            navController.navigate("add_inventory_screen")
                        })

                        DropdownMenuItem(text = { Text("Update Inventory") }, onClick = {
                            showMenu = false
                            selectedItemName?.let { name ->
                                navController.navigate("update_inventory_screen/$name")
                            }
                        })

                        DropdownMenuItem(text = { Text("Delete Inventory") }, onClick = {
                            showMenu = false
                            navController.navigate("delete_inventory_screen")
                        })
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "Inventory Items:", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(10.dp))

                if (inventoryItems.isEmpty()) {
                    Text(text = "No inventory available.", style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn {
                        items(inventoryItems) { item ->
                            InventoryItemCard(item) {
                                selectedItemName = item.name
                                showMenu = true
                            }
                        }
                    }
                }
            }
        }
    }
}

// Inventory Item Card (with Click Action for Update)
@Composable
fun InventoryItemCard(item: InventoryItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() } // Click to select item for update
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${item.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Type: ${item.type}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Price: ${item.price}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Status: ${item.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
