package com.example.mytailorsapp.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.CustomerRepository
import com.example.mytailorsapp.databinding.ActivityCustomerProfileBinding
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import androidx.core.view.GravityCompat
import com.example.mytailorsapp.data.repository.CartRepository
import com.example.mytailorsapp.data.repository.MaterialRepository
import com.example.mytailorsapp.data.repository.WishlistRepository
import com.example.mytailorsapp.ui.auth.LoginActivity
import com.example.mytailorsapp.ui.common.CustomerNavigationHelper

class CustomerProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerProfileBinding
    private lateinit var viewModel: CustomerViewModel
    private var customerId: String = ""

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

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

        val sharedPref = getSharedPreferences("MyTailorPrefs", MODE_PRIVATE)
        customerId = sharedPref.getString("loggedInCustomerId", null) ?: ""

        viewModel = CustomerViewModelFactory(
            CustomerRepository(),
            MaterialRepository(),
            CartRepository(),
            WishlistRepository()
        ).create(CustomerViewModel::class.java)

        lifecycleScope.launch {
            viewModel.fetchCustomerById(customerId)
        }

        viewModel.selectedCustomer.observe(this) { customer ->
            customer?.let {
                binding.tvName.text = it.name
                binding.tvEmail.text = it.email
                binding.tvContact.text = it.contact
                binding.tvAddress.text = it.address

                if (!it.profileImageUrl.isNullOrEmpty()) {
                    binding.imgProfile.load(it.profileImageUrl)
                }
            }
        }

        binding.btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateCustomerProfileActivity::class.java)
            intent.putExtra("customerId", customerId)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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
