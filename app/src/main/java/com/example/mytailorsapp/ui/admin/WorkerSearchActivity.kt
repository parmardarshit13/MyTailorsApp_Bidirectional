package com.example.mytailorsapp.ui.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.WorkerRepository
import com.example.mytailorsapp.ui.adapter.WorkerAdapter
import com.example.mytailorsapp.ui.common.AdminNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.example.mytailorsapp.viewmodel.WorkerViewModel
import com.example.mytailorsapp.viewmodel.WorkerViewModelFactory
import com.google.android.material.navigation.NavigationView

class WorkerSearchActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    private lateinit var etSearchName: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnCancel: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var workerAdapter: WorkerAdapter
    private lateinit var viewModel: WorkerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_search)

        // Drawer & Toolbar
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navIcon: ImageView = findViewById(R.id.custom_nav_icon)
        navIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        SidebarMenuHelper.setupAdminMenu(navigationView, drawerLayout) { option ->
            AdminNavigationHelper.handleNavigation(this, option)
        }

        // UI Init
        etSearchName = findViewById(R.id.etSearchName)
        btnSearch = findViewById(R.id.btnSearch)
        btnCancel = findViewById(R.id.btnCancel)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        workerAdapter = WorkerAdapter()
        recyclerView.adapter = workerAdapter

        // ViewModel Init
        viewModel = ViewModelProvider(
            this,
            WorkerViewModelFactory(WorkerRepository())
        )[WorkerViewModel::class.java]

        // Observe selected worker
        viewModel.selectedWorker.observe(this) { worker ->
            if (worker != null) {
                workerAdapter.submitList(listOf(worker)) // Since it's a single result
            } else {
                Toast.makeText(this, "No worker found", Toast.LENGTH_SHORT).show()
                workerAdapter.submitList(emptyList())
            }
        }

        // Search Click
        btnSearch.setOnClickListener {
            val name = etSearchName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter worker name", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.fetchWorkerByName(name)
            }
        }

        // Cancel Click
        btnCancel.setOnClickListener {
            etSearchName.text.clear()
            workerAdapter.submitList(emptyList())
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
