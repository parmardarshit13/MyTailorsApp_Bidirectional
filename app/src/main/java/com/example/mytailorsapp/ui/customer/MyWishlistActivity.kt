package com.example.mytailorsapp.ui.customer

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.*
import com.example.mytailorsapp.ui.adapter.WishlistAdapter
import com.example.mytailorsapp.ui.common.CustomerNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MyWishlistActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: WishlistAdapter
    private lateinit var viewModel: CustomerViewModel

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        recyclerView = findViewById(R.id.recyclerViewWishlist)
        progressBar = findViewById(R.id.progressBarWishlist)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        // 🔘 Navigation drawer icon listener
        findViewById<ImageView>(R.id.custom_nav_icon).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 🔘 Sidebar navigation
        SidebarMenuHelper.setupCustomerMenu(navigationView, drawerLayout) { option ->
            CustomerNavigationHelper.handleNavigation(this, option)
        }

        // 🔘 WishlistAdapter setup
        adapter = WishlistAdapter(
            onItemClick = { item ->
                Toast.makeText(this, "Viewing ${item.name}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { item ->
                viewModel.removeFromWishlist(item.id, userId)
                Toast.makeText(this, "${item.name} removed from wishlist", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 🔘 ViewModel setup
        viewModel = ViewModelProvider(
            this,
            CustomerViewModelFactory(
                CustomerRepository(),
                MaterialRepository(),
                CartRepository(),
                WishlistRepository()
            )
        )[CustomerViewModel::class.java]

        // 🔘 Load wishlist after getting user ID
        lifecycleScope.launch {
            viewModel.getLoggedInCustomerId(this@MyWishlistActivity) { id ->
                if (id != null) {
                    userId = id
                    fetchWishlist()
                } else {
                    Toast.makeText(this@MyWishlistActivity, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchWishlist()
    }

    private fun fetchWishlist() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchWishlist(userId) {
                adapter.submitList(it)
                progressBar.visibility = View.GONE
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
