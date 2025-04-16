package com.example.mytailorsapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.mytailorsapp.viewmodel.AdminViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    context: Context,
    customerViewModel: CustomerViewModel,
    workerViewModel: WorkerViewModel,
    inventoryViewModel: InventoryViewModel,
    adminViewModel: AdminViewModel,
    userId: Int = 0,
    isDark: Boolean,
    onToggleTheme: () -> Unit)
{
    NavHost(navController = navController, startDestination = "login_screen") {
        authGraph(navController, context)
        dashboardGraph(navController, context, customerViewModel, userId, isDark, onToggleTheme)
        adminGraph(navController, workerViewModel, inventoryViewModel, adminViewModel, userId, isDark, onToggleTheme)
    }
}
