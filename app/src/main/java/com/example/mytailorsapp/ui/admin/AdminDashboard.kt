package com.example.mytailorsapp.ui.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mytailorsapp.data.repository.WorkerRepository
import com.example.mytailorsapp.ui.common.SidebarDrawer
import com.example.mytailorsapp.viewmodel.WorkerViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModelFactory
import kotlinx.coroutines.launch

class AdminDashboard : ComponentActivity() {
    private val viewModel: WorkerViewModel by viewModels {
        WorkerViewModelFactory(WorkerRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var isDark by remember { mutableStateOf(false) } // ✅ State for theme toggle

            AdminDashboardUI(
                viewModel,
                navController,
                userId = 0,
                isDark = isDark,
                onToggleTheme = { isDark = !isDark })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardUI(
    viewModel: WorkerViewModel,
    navController: NavController,
    userId: Int = 0,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllWorkers()
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
                isAdminDashboard = true // ✅ use this flag instead
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Admin Dashboard") },
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
                    .padding(16.dp)
            ) {
                Text("Welcome, Admin", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Select an option from the sidebar menu", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
