package com.example.mytailorsapp.ui.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mytailorsapp.data.repository.CustomerRepository
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.data.repository.MaterialRepository
import com.example.mytailorsapp.ui.common.SidebarDrawer
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory
import kotlinx.coroutines.launch

class CustomerDashboard : ComponentActivity() {
    private val viewModel: CustomerViewModel by viewModels {
        CustomerViewModelFactory(
            CustomerRepository(),
            MaterialRepository(),
            InventoryRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var isDark by remember { mutableStateOf(false) } // ✅ State for theme toggle

            CustomerDashboardUI(
                viewModel = viewModel,
                navController = navController,
                isDark = isDark,
                onToggleTheme = { isDark = !isDark }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDashboardUI(
    viewModel: CustomerViewModel,
    navController: NavController,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val customer by viewModel.selectedCustomer.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }
    var userId by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getLoggedInCustomerId { id ->
            id?.let {
                userId = it
                viewModel.fetchCustomerById(it)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
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
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Customer Dashboard") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome, ${customer?.name ?: "User"}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Select an option from the sidebar menu", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
