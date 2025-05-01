package com.example.mytailorsapp.ui.admin

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.WorkerRepository
import com.example.mytailorsapp.ui.adapter.WorkerAdapter
import com.example.mytailorsapp.ui.common.AdminNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class AdminManageWorkersActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var workerAdapter: WorkerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_workers)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerViewWorkers)

        setSupportActionBar(toolbar)

        val navIcon: ImageView = findViewById(R.id.custom_nav_icon)
        navIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        SidebarMenuHelper.setupAdminMenu(navigationView, drawerLayout) { option ->
            AdminNavigationHelper.handleNavigation(this, option)
        }

        // RecyclerView setup
        workerAdapter = WorkerAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = workerAdapter

        // Load workers
        loadWorkers()
    }

    private fun loadWorkers() {
        lifecycleScope.launch {
            try {
                val workers = WorkerRepository().getAllWorkers()
                workerAdapter.submitList(workers)
            } catch (e: Exception) {
                Toast.makeText(this@AdminManageWorkersActivity, "Failed to load workers", Toast.LENGTH_LONG).show()
                e.printStackTrace()
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
