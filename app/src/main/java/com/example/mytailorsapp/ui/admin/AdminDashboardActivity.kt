package com.example.mytailorsapp.ui.admin

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.InventoryStatus
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.ui.common.AdminNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    // Views to update
    private lateinit var tvTotalInventory: TextView
    private lateinit var tvPendingOrders: TextView
    private lateinit var tvTodayDeliveries: TextView
    private lateinit var tvLowStock: TextView

    private val repository = InventoryRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Toolbar + drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        val navIcon: ImageView = findViewById(R.id.custom_nav_icon)
        navIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        SidebarMenuHelper.setupAdminMenu(navView, drawerLayout) { option ->
            AdminNavigationHelper.handleNavigation(this, option)
        }

        // Initialize text views
        tvTotalInventory = findViewById(R.id.tvTotalInventory)
        tvPendingOrders = findViewById(R.id.tvPendingOrders)
        tvTodayDeliveries = findViewById(R.id.tvTodayDeliveries)
        tvLowStock = findViewById(R.id.tvLowStock)

        // Load stats
        loadDashboardStats()
    }

    private fun loadDashboardStats() {
        lifecycleScope.launch {
            try {
                val inventoryItems = repository.getAllAdminInventory()

                val total = inventoryItems.size
                val pending = inventoryItems.count { it.status == InventoryStatus.PENDING }
                val today = inventoryItems.count {
                    val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    val itemDate = it.deadline?.let { d ->
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(d)
                    }
                    todayStr == itemDate
                }
                val lowStock = inventoryItems.count { it.quantity <= 2 }

                tvTotalInventory.text = "Total Inventory: $total"
                tvPendingOrders.text = "Pending Orders: $pending"
                tvTodayDeliveries.text = "Today's Deliveries: $today"
                tvLowStock.text = "Low Stock Items: $lowStock"

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@AdminDashboardActivity, "Error loading stats: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
