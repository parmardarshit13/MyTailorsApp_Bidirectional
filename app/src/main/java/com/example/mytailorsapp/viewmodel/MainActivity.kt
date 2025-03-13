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
            val customerDao = AppDatabase.getDatabase(this).customerDao()
            val inventoryDao = AppDatabase.getDatabase(this).inventoryDao()
            val workerDao = AppDatabase.getDatabase(this).workerDao()

            // ✅ Create ViewModel at activity level
            val customerViewModel: CustomerViewModel = viewModel(
                factory = CustomerViewModelFactory(customerDao, inventoryDao)
            )

            val workerViewModel: WorkerViewModel = viewModel(
                factory = WorkerViewModelFactory(workerDao)
            )

            // ✅ Use different navigation files for better structure
            AppNavigation(navController, this, customerViewModel, workerViewModel)
        }
    }
}
