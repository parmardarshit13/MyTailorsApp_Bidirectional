package com.example.mytailorsapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mytailorsapp.ui.admin.AddInventoryScreen
import com.example.mytailorsapp.ui.admin.AdminDashboardUI
import com.example.mytailorsapp.ui.admin.AdminProfileScreen
import com.example.mytailorsapp.ui.admin.DeleteInventoryScreen
import com.example.mytailorsapp.ui.admin.ManageInventoryScreen
import com.example.mytailorsapp.ui.admin.ManageShopsScreen
import com.example.mytailorsapp.ui.admin.ManageWorkersScreen
import com.example.mytailorsapp.ui.admin.UpdateInventoryScreen
import com.example.mytailorsapp.ui.admin.WorkerSearchScreen
import com.example.mytailorsapp.viewmodel.AdminViewModel
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModel

fun NavGraphBuilder.adminGraph(
    navController: NavHostController,
    workerViewModel: WorkerViewModel,
    inventoryViewModel: InventoryViewModel,
    adminViewModel: AdminViewModel,
    userId: Int = 0,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    // Admin Routes
    composable("admin_dashboard") { AdminDashboardUI(workerViewModel, navController, userId, isDark, onToggleTheme) }
    composable("admin_profile_screen") { AdminProfileScreen(navController, adminViewModel) }
    composable("worker_search_screen") { WorkerSearchScreen(navController, userId, isDark, onToggleTheme) }
    composable("manage_workers_screen") { ManageWorkersScreen(navController, userId, isDark, onToggleTheme) }
    composable("manage_shops_screen") { ManageShopsScreen(navController, userId, isDark, onToggleTheme) }
    composable("manage_inventory_screen") { ManageInventoryScreen(navController, inventoryViewModel, userId, isDark, onToggleTheme) }

    // ✅ Add navigation for inventory operations
    composable("add_inventory_screen") { AddInventoryScreen(navController, inventoryViewModel) }

    composable("update_inventory_screen/{inventoryName}") { backStackEntry ->
        val inventoryName = backStackEntry.arguments?.getString("inventoryName")
        UpdateInventoryScreen(navController, inventoryViewModel, inventoryName)
    }
    composable("delete_inventory_screen") { DeleteInventoryScreen(inventoryViewModel) }
}
