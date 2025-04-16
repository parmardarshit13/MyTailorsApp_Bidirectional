package com.example.mytailorsapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.ui.common.SidebarDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageShopsScreen(
    navController: NavController,
    userId: Int = 0,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }

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
                isAdminDashboard = true
            )
        }
    ){
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Manage Shops") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Shop Management Feature (To be implemented)", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
