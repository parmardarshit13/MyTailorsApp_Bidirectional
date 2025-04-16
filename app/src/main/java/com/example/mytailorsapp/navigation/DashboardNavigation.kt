package com.example.mytailorsapp.navigation

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mytailorsapp.ui.auth.LoginScreenUI
import com.example.mytailorsapp.ui.customer.*
import com.example.mytailorsapp.viewmodel.CustomerViewModel

fun NavGraphBuilder.dashboardGraph(
    navController: NavHostController,
    context: Context,
    customerViewModel: CustomerViewModel,
    userId: Int = 0,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    // ✅ Main Dashboard
    composable("customer_dashboard") {
        CustomerDashboardUI(
            viewModel = customerViewModel,
            navController = navController,
            isDark = isDark,
            onToggleTheme = onToggleTheme
        )
    }

    // ✅ Profile (Read-Only)
    composable(
        route = "profile_screen/{customerId}",
        arguments = listOf(navArgument("customerId") { type = NavType.IntType })
    ) { backStackEntry ->
        val customerId = backStackEntry.arguments?.getInt("customerId") ?: 0
        ProfileScreen(navController, customerId, customerViewModel)
    }

    // ✅ Update Profile
    composable(
        route = "update_profile_screen/{customerId}",
        arguments = listOf(navArgument("customerId") { type = NavType.IntType })
    ) { backStackEntry ->
        val customerId = backStackEntry.arguments?.getInt("customerId") ?: 0
        UpdateProfileScreen(navController, customerId, customerViewModel)
    }

    // ✅ Login (Redirect after logout)
    composable("loginScreen") {
        LoginScreenUI(navController, context)
    }

    // ✅ Shop Search
    composable("shop_search_screen") {
        ShopSearchScreen(
            navController = navController,
            userId = userId,
            isDark = isDark,
            onToggleTheme = onToggleTheme
        )
    }

    // ✅ Category Screen (Sidebar enabled)
    composable("category_screen") {
        CategoryScreen(
            navController = navController,
            userId = userId,
            isDark = isDark,
            onToggleTheme = onToggleTheme
        )
    }

    // ✅ Material List Based on Category
    composable("material_screen/{category}") { backStackEntry ->
        val category = backStackEntry.arguments?.getString("category") ?: "All"
        MaterialScreen(
            navController = navController,
            category = category,
            viewModel = customerViewModel,
            userId = userId,
            isDark = isDark,
            onToggleTheme = onToggleTheme
        )
    }

    // ✅ Virtual Try-On
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

    // ✅ Inventory Tracking
    composable("inventory_screen") {
        InventoryScreen(
            navController = navController,
            viewModel = customerViewModel,
            userId = userId,
            isDark = isDark,
            onToggleTheme = onToggleTheme
        )
    }
}
