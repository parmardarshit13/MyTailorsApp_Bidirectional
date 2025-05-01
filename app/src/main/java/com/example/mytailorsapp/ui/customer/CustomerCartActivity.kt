package com.example.mytailorsapp.ui.customer

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.*
import com.example.mytailorsapp.databinding.ActivityCustomerCartBinding
import com.example.mytailorsapp.ui.adapter.CartAdapter
import com.example.mytailorsapp.ui.common.CustomerNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class CustomerCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerCartBinding
    private lateinit var adapter: CartAdapter
    private lateinit var viewModel: CustomerViewModel
    private var userId: String = ""

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        toolbar = binding.toolbar

        setSupportActionBar(toolbar)

        // ✅ Make the custom icon clickable
        val navIcon: ImageView = findViewById(R.id.custom_nav_icon)
        navIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 🔘 Sidebar navigation
        SidebarMenuHelper.setupCustomerMenu(navigationView, drawerLayout) { option ->
            CustomerNavigationHelper.handleNavigation(this, option)
        }

        adapter = CartAdapter(
            onQuantityChange = {
               updateTotal()
            },
            onDelete = {
                viewModel.removeCartItem(it.id, userId)
            }
        )

        binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCart.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            CustomerViewModelFactory(
                CustomerRepository(),
                MaterialRepository(),
                CartRepository(),
                WishlistRepository()
            )
        )[CustomerViewModel::class.java]

        // ✅ Observe cart items LiveData
        viewModel.cartItems.observe(this) { items ->
            adapter.submitList(items)
            updateTotal()
        }

        // ✅ Get logged-in customer ID and trigger cart fetch
        lifecycleScope.launch {
            viewModel.getLoggedInCustomerId(this@CustomerCartActivity) { id ->
                if (id != null) {
                    userId = id
                    viewModel.fetchCartItems(userId) // Fire and forget; LiveData will update UI
                }
            }
        }

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(this, "Proceeding to checkout", Toast.LENGTH_SHORT).show()
            // TODO: Proceed to checkout/payment screen
        }
    }

    private fun updateTotal() {
        val total = adapter.calculateTotal()
        binding.tvTotalAmount.text = "Total: ₹$total"
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
