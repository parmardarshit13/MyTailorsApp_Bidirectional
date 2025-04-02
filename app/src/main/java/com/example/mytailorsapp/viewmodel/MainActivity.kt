package com.example.mytailorsapp.viewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mytailorsapp.database.AppDatabase
import com.example.mytailorsapp.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val database = AppDatabase.getDatabase(this)

            val customerDao = database.customerDao()
            val inventoryDao = database.inventoryDao()
            val workerDao = database.workerDao()
            val materialDao = database.materialDao()

            // ✅ Create ViewModels at activity level
            val customerViewModel: CustomerViewModel = viewModel(
                factory = CustomerViewModelFactory(customerDao, materialDao, inventoryDao)
            )

            val workerViewModel: WorkerViewModel = viewModel(
                factory = WorkerViewModelFactory(workerDao)
            )

            val inventoryViewModel: InventoryViewModel = viewModel(
                factory = InventoryViewModelFactory(inventoryDao)
            )

            // ✅ Pass inventoryViewModel to AppNavigation()
            AppNavigation(navController, this, customerViewModel, workerViewModel, inventoryViewModel)
        }
    }
}
