package com.example.mytailorsapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModel

@Composable
fun AppNavigation(navController: NavHostController, context: Context, customerViewModel: CustomerViewModel, workerViewModel: WorkerViewModel, inventoryViewModel: InventoryViewModel) {
    NavHost(navController = navController, startDestination = "login_screen") {
        authGraph(navController, context) // ✅ Make sure this is included correctly
        dashboardGraph(navController, context, customerViewModel ) // ✅ Dashboard graph included correctly
        adminGraph(navController, workerViewModel, inventoryViewModel)
    }
}
