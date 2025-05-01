package com.example.mytailorsapp.ui.customer

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.CategoryRepository
import com.example.mytailorsapp.databinding.ActivityCategoryBinding
import com.example.mytailorsapp.ui.adapter.CategoryAdapter
import com.example.mytailorsapp.ui.common.CustomerNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryRepository = CategoryRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // ✅ Make the custom icon clickable
        val navIcon: ImageView = findViewById(R.id.custom_nav_icon)
        navIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        SidebarMenuHelper.setupCustomerMenu(binding.navView, binding.drawerLayout) { option ->
            CustomerNavigationHelper.handleNavigation(this, option)
        }

        binding.recyclerViewCategories.layoutManager = GridLayoutManager(this, 2)

        categoryAdapter = CategoryAdapter { category ->
            val intent = Intent(this, MaterialActivity::class.java)
            intent.putExtra("category", category.name) // ✅ Make sure this matches MaterialActivity
            startActivity(intent)
        }

        binding.recyclerViewCategories.adapter = categoryAdapter
        loadCategories()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            try {
                val list = categoryRepository.getAllCategories()
                categoryAdapter.submitList(list)
            } catch (_: Exception) {
                Toast.makeText(this@CategoryActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
