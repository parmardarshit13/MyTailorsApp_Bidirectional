package com.example.mytailorsapp.ui.customer

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.CartRepository
import com.example.mytailorsapp.data.repository.CustomerRepository
import com.example.mytailorsapp.data.repository.MaterialRepository
import com.example.mytailorsapp.data.repository.WishlistRepository
import com.example.mytailorsapp.ui.common.CustomerNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class CustomerDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var tvWelcome: TextView
    private lateinit var viewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_dashboard)

        viewModel = CustomerViewModelFactory(
            CustomerRepository(),
            MaterialRepository(),
            CartRepository(),
            WishlistRepository()
        ).create(CustomerViewModel::class.java)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar       = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        // ✅ Make the custom icon clickable
        val navIcon: ImageView = findViewById(R.id.custom_nav_icon)
        navIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Populate menu and handle clicks
        SidebarMenuHelper.setupCustomerMenu(navigationView, drawerLayout) { option ->
            CustomerNavigationHelper.handleNavigation(this, option)
        }

        tvWelcome = findViewById(R.id.tvWelcomeUser)

        // Load logged-in customer and observe LiveData for name
        lifecycleScope.launch {
            viewModel.getLoggedInCustomerId(this@CustomerDashboardActivity) { customerId ->
                if (customerId != null) {
                    viewModel.fetchCustomerById(customerId)
                    viewModel.selectedCustomer.observe(this@CustomerDashboardActivity) { customer ->
                        tvWelcome.text = "Welcome, ${customer?.name ?: "User"}"
                    }
                } else {
                    tvWelcome.text = "Welcome, Guest"
                }
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
