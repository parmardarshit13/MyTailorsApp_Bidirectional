package com.example.mytailorsapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mytailorsapp.ui.admin.*
import com.example.mytailorsapp.ui.customer.*
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModel
import com.example.mytailorsapp.R

fun NavGraphBuilder.dashboardGraph(navController: NavHostController, customerViewModel: CustomerViewModel, workerViewModel: WorkerViewModel) {
    // Customer Routes
    composable("customer_dashboard") { CustomerDashboardUI(customerViewModel, navController) }
    composable("profile_screen/{customerId}") { backStackEntry ->
        val customerId = backStackEntry.arguments?.getString("customerId")?.toIntOrNull() ?: 0
        ProfileScreen(navController, customerId, customerViewModel)
    }
    composable("shop_search_screen") { ShopSearchScreen(navController) }
    composable("material_screen") { MaterialScreen(navController) }
    composable("virtual_clothing_screen/{materialName}/{materialResId}") { backStackEntry ->
        val materialName = backStackEntry.arguments?.getString("materialName") ?: "Unknown"
        val materialResIdString = backStackEntry.arguments?.getString("materialResId") ?: "0"

        val materialResId = materialResIdString.toIntOrNull() ?: R.drawable.cotton // ✅ Provide a default image

        VirtualClothingScreen(materialName, materialResId, navController)
    }

    composable("inventory_screen") { InventoryScreen(navController, customerViewModel) }

    // Admin Routes
    composable("admin_dashboard") { AdminDashboardUI(workerViewModel, navController) }
    composable("admin_profile_screen") { AdminProfileScreen(navController) }
    composable("worker_search_screen") { WorkerSearchScreen(navController) }
    composable("manage_workers_screen") { ManageWorkersScreen(navController) }
    composable("manage_shops_screen") { ManageShopsScreen(navController) }
    composable("manage_inventory_screen") { ManageInventoryScreen(navController) }
}
