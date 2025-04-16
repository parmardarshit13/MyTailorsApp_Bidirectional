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
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.data.models.InventoryStatus  // ✅ Import the enum
import com.example.mytailorsapp.ui.common.SidebarDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    viewModel: CustomerViewModel = viewModel(),
    userId: Int = 0,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val inventoryItems by viewModel.inventoryItems.collectAsState(initial = emptyList())

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
                isAdminDashboard = false // ✅ default for customers
            )
        }
    ){
        Scaffold(
            topBar = { TopAppBar(title = { Text("Your Orders") }) }
        ) { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                items(inventoryItems) { item ->
                    InventoryCard(item)
                }
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
            Text("Type: ${item.type}", style = MaterialTheme.typography.bodyMedium)
            Text("Price: $${item.price}", style = MaterialTheme.typography.bodyMedium)

            // ✅ Use the InventoryStatus enum properly
            Text(
                text = "Status: ${getStatusText(item.status)}",
                style = MaterialTheme.typography.bodyMedium,
                color = getStatusColor(item.status)  // Color based on status
            )
        }
    }
}

// ✅ Function to convert enum to a readable string
fun getStatusText(status: InventoryStatus): String {
    return when (status) {
        InventoryStatus.IN_PROGRESS -> "In Progress"
        InventoryStatus.COMPLETED -> "Completed"
        InventoryStatus.PENDING -> "Pending"
    }
}

// ✅ Function to assign colors based on status
@Composable
fun getStatusColor(status: InventoryStatus): androidx.compose.ui.graphics.Color {
    return when (status) {
        InventoryStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
        InventoryStatus.COMPLETED -> MaterialTheme.colorScheme.secondary
        InventoryStatus.PENDING -> MaterialTheme.colorScheme.error
    }
}
