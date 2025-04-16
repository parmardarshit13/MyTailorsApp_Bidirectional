package com.example.mytailorsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mytailorsapp.data.repository.AdminRepository
import com.example.mytailorsapp.data.repository.CustomerRepository
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.data.repository.MaterialRepository
import com.example.mytailorsapp.data.repository.WorkerRepository
import com.example.mytailorsapp.navigation.AppNavigation
import com.example.mytailorsapp.ui.theme.MyTailorsAppTheme
import com.example.mytailorsapp.viewmodel.AdminViewModel
import com.example.mytailorsapp.viewmodel.AdminViewModelFactory
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import com.example.mytailorsapp.viewmodel.InventoryViewModelFactory
import com.example.mytailorsapp.viewmodel.ThemeViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // ✅ ThemeViewModel controls dark/light theme
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme = themeViewModel.isDarkTheme.collectAsState().value

            // ✅ Database setup
            val customerRepository = CustomerRepository()
            val materialRepository = MaterialRepository()
            val inventoryRepository = InventoryRepository()
            val workerRepository = WorkerRepository()
            val adminRepository = AdminRepository()

            // ✅ ViewModels scoped to MainActivity
            val customerViewModel: CustomerViewModel = viewModel(
                factory = CustomerViewModelFactory(
                    customerRepository,
                    materialRepository,
                    inventoryRepository
                )
            )

            val workerViewModel: WorkerViewModel = viewModel(
                factory = WorkerViewModelFactory(workerRepository)
            )

            val inventoryViewModel: InventoryViewModel = viewModel(
                factory = InventoryViewModelFactory(inventoryRepository)
            )

            val adminViewModel: AdminViewModel = viewModel(
                factory = AdminViewModelFactory(adminRepository)
            )

            // ✅ Navigation controller
            val navController = rememberNavController()

            // ✅ Wrap all content in theme
            MyTailorsAppTheme(darkTheme = isDarkTheme) {
                AppNavigation(
                    navController = navController,
                    context = this,
                    customerViewModel = customerViewModel,
                    workerViewModel = workerViewModel,
                    inventoryViewModel = inventoryViewModel,
                    adminViewModel = adminViewModel,
                    isDark = isDarkTheme,
                    onToggleTheme = { themeViewModel.toggleTheme() }
                )
            }
        }
    }
}