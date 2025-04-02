package com.example.mytailorsapp.navigation

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mytailorsapp.ui.customer.CategoryScreen
import com.example.mytailorsapp.ui.customer.CustomerDashboardUI
import com.example.mytailorsapp.ui.customer.InventoryScreen
import com.example.mytailorsapp.ui.customer.MaterialScreen
import com.example.mytailorsapp.ui.customer.ProfileScreen
import com.example.mytailorsapp.ui.customer.ShopSearchScreen
import com.example.mytailorsapp.ui.customer.UpdateProfileScreen
import com.example.mytailorsapp.ui.customer.VirtualClothingScreen
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.ui.auth.LoginScreenUI

fun NavGraphBuilder.dashboardGraph(
    navController: NavHostController,
    context: Context,
    customerViewModel: CustomerViewModel,

    ) {
    // Customer Routes
    composable("customer_dashboard") { CustomerDashboardUI(customerViewModel, navController) }

    // Profile Screen (Read-Only Mode)
    composable(
        route = "profileScreen/{customerId}",
        arguments = listOf(navArgument("customerId") { type = NavType.IntType })
    ) { backStackEntry ->
        val customerId = backStackEntry.arguments?.getInt("customerId") ?: 0
        ProfileScreen(navController, customerId)
    }

    // Update Profile Screen (Editable Mode)
    composable(
        route = "updateProfileScreen/{customerId}",
        arguments = listOf(navArgument("customerId") { type = NavType.IntType })
    ) { backStackEntry ->
        val customerId = backStackEntry.arguments?.getInt("customerId") ?: 0
        UpdateProfileScreen(navController, customerId)
    }

    // Login Screen (Redirect after logout)
    composable("loginScreen") {
        LoginScreenUI(navController, context)
    }

    composable("shop_search_screen") { ShopSearchScreen(navController) }
    composable("category_screen") { CategoryScreen(navController) }
    composable("material_screen/{category}") { backStackEntry ->
        val category = backStackEntry.arguments?.getString("category") ?: "All"
        MaterialScreen(navController, category)
    }
    composable(
        "virtual_clothing_screen/{materialName}/{materialResId}",
        arguments = listOf(
            navArgument("materialName") { type = NavType.StringType },
            navArgument("materialResId") { type = NavType.IntType }
        )
    ) { backStackEntry ->
        VirtualClothingScreen(
            materialName = backStackEntry.arguments?.getString("materialName") ?: "",
            materialResId = backStackEntry.arguments?.getInt("materialResId") ?: 0,
            navController = navController
        )
    }

    composable("inventory_screen") { InventoryScreen(navController, customerViewModel) }
}
