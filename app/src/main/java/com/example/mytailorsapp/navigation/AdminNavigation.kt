package com.example.mytailorsapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mytailorsapp.ui.admin.*
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModel

fun NavGraphBuilder.adminGraph(
    navController: NavHostController,
    workerViewModel: WorkerViewModel,
    inventoryViewModel: InventoryViewModel
) {
    // Admin Routes
    composable("admin_dashboard") { AdminDashboardUI(workerViewModel, navController) }
    composable("admin_profile_screen") { AdminProfileScreen(navController) }
    composable("worker_search_screen") { WorkerSearchScreen(navController) }
    composable("manage_workers_screen") { ManageWorkersScreen(navController) }
    composable("manage_shops_screen") { ManageShopsScreen(navController) }
    composable("manage_inventory_screen") { ManageInventoryScreen(navController, inventoryViewModel) }

    // ✅ Add navigation for inventory operations
    composable(
        route = "addInventoryScreen/{customerId}",
        arguments = listOf(navArgument("customerId") { type = NavType.IntType })
    ) { backStackEntry ->
        val customerId = backStackEntry.arguments?.getInt("customerId") ?: 0
        AddInventoryScreen(navController, inventoryViewModel, customerId)
    }

    composable("update_inventory_screen/{inventoryName}") { backStackEntry ->
        val inventoryName = backStackEntry.arguments?.getString("inventoryName")
        UpdateInventoryScreen(navController, inventoryViewModel, inventoryName)
    }
    composable("delete_inventory_screen") { DeleteInventoryScreen(navController, inventoryViewModel) }
}
