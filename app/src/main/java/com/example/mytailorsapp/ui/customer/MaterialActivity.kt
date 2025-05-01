package com.example.mytailorsapp.ui.customer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.MaterialRepository
import com.example.mytailorsapp.databinding.ActivityMaterialBinding
import com.example.mytailorsapp.ui.adapter.MaterialAdapter
import com.example.mytailorsapp.ui.common.CustomerNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MaterialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaterialBinding
    private lateinit var adapter: MaterialAdapter
    private val repository = MaterialRepository()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    private var category: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialBinding.inflate(layoutInflater)
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

        // ✅ Setup drawer with customer options
        SidebarMenuHelper.setupCustomerMenu(navigationView, drawerLayout) { option ->
            CustomerNavigationHelper.handleNavigation(this, option)
        }

        // ✅ Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MaterialAdapter()
        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        // ✅ Always fetch fresh category from intent
        category = intent.getStringExtra("category") ?: ""
        binding.tvTitle.text = if (category.isNotBlank()) "$category Materials" else "All Materials"

        // ✅ Load materials from Firestore based on category
        loadMaterials()
    }

    private fun loadMaterials() {
        lifecycleScope.launch {
            val filtered = if (category.isBlank()) {
                repository.getAllMaterials()
            } else {
                repository.getMaterialsByCategory(category.trim())
            }

            binding.tvEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(filtered)
            Log.d("MaterialActivity", "Category for filter: $category")
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
